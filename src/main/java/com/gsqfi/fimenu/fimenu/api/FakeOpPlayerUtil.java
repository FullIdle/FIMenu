package com.gsqfi.fimenu.fimenu.api;

import lombok.Getter;
import lombok.SneakyThrows;
import me.fullidle.ficore.ficore.common.SomeMethod;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.implementation.FieldAccessor;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.implementation.MethodCall;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.FieldValue;
import net.bytebuddy.implementation.bind.annotation.Pipe;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.matcher.ElementMatchers;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;


/**
 * 借鉴Taboolib的bukkit-fake-op
 */
@Getter
public class FakeOpPlayerUtil {
    @Getter
    private static final FakeOpPlayerUtil INSTANCE = new FakeOpPlayerUtil();

    private final Class<?> fakeOpPlayerClass;
    private final Constructor<?> fakeOpPlayerConstructor;

    private final Class<?> craftPlayerClass;
    private final Class<?> craftServerClass;
    private final Class<?> entityPlayerClass;


    @SneakyThrows
    private FakeOpPlayerUtil() {
        String nmsVersion = SomeMethod.getNmsVersion();
        craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + nmsVersion + ".entity.CraftPlayer");
        craftServerClass = Class.forName("org.bukkit.craftbukkit." + nmsVersion + ".CraftServer");
        entityPlayerClass = craftPlayerClass.getDeclaredMethod("getHandle").getReturnType();

        this.fakeOpPlayerClass = new ByteBuddy()
                .subclass(craftPlayerClass)
                .defineField("craftPlayer", craftPlayerClass, Visibility.PUBLIC)
                .defineConstructor(Visibility.PUBLIC)
                .withParameters(craftServerClass, entityPlayerClass, craftPlayerClass)
                .intercept(
                        MethodCall.invoke(craftPlayerClass.getDeclaredConstructor(craftServerClass, entityPlayerClass))
                                .withArgument(0, 1)
                                .andThen(FieldAccessor.ofField("craftPlayer").setsArgumentAt(2))
                )
                .method(ElementMatchers.isDeclaredBy(craftPlayerClass))
                .intercept(
                        MethodDelegation.withDefaultConfiguration()
                                .withBinders(Pipe.Binder.install(Delegation.class))
                                .to(this)
                )
                .method(ElementMatchers.named("isOp"))
                .intercept(FixedValue.value(true))
                .method(ElementMatchers.named("hasPermission"))
                .intercept(FixedValue.value(true))
                .make().load(this.getClass().getClassLoader()).getLoaded();
        this.fakeOpPlayerConstructor = this.fakeOpPlayerClass.getDeclaredConstructor(craftServerClass, entityPlayerClass, craftPlayerClass);
    }

    private Player tempCraftPlayer;

    @SneakyThrows
    public Player createProxy(Player player) {
        tempCraftPlayer = player;
        return (Player) fakeOpPlayerConstructor.newInstance(player.getServer()
                , craftPlayerClass.getDeclaredMethod("getHandle").invoke(player)
                , player);
    }

    @RuntimeType
    public Object any(@Pipe Delegation<Object, Object> delegation, @FieldValue("craftPlayer") Object craftPlayer) {
        return delegation.to(craftPlayer == null ? tempCraftPlayer : craftPlayer);
    }

    public interface Delegation<T, V> {
        T to(V v);
    }
}
