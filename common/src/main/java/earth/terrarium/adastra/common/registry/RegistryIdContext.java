package earth.terrarium.adastra.common.registry;

import net.minecraft.resources.Identifier;

/**
 * ThreadLocal context for passing the registration ID to Properties mixins
 * during item/block construction in ResourcefulLib's registry.
 */
public class RegistryIdContext {
    public static final ThreadLocal<Identifier> CURRENT_ID = new ThreadLocal<>();
}
