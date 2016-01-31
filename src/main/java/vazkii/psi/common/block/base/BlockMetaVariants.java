/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [31/01/2016, 19:15:27 (GMT)]
 */
package vazkii.psi.common.block.base;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelBlockDefinition.Variants;
import net.minecraft.util.IStringSerializable;

public class BlockMetaVariants<T extends Enum<T> & IStringSerializable> extends BlockMod implements IVariantEnumHolder<T> {

	public static Class temporaryVariantsEnum; // This is a massive hack, but such is life with constructors
	public static PropertyEnum temporaryVariantProp;

	public final Class<T> variantsEnum;
	public final PropertyEnum<T> variantProp;
	
	public BlockMetaVariants(String name, Material materialIn, Class<T> variantsEnum) {
		super(name, materialIn, asVariantArray(variantsEnum));

		this.variantsEnum = variantsEnum;
		this.variantProp = temporaryVariantProp;
		setDefaultState(blockState.getBaseState().withProperty(variantProp, variantsEnum.getEnumConstants()[0]));
	}
	
	@Override
	public BlockState createBlockState() {
		return new BlockState(this, temporaryVariantProp);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(variantProp == null ? temporaryVariantProp : variantProp).ordinal();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		if(meta >= variantsEnum.getEnumConstants().length)
			meta = 0;
		
		return getDefaultState().withProperty(variantProp, variantsEnum.getEnumConstants()[meta]);
	}
	
	@Override
	public Class<T> getVariantEnum() {
		return variantsEnum;
	}
	
	public static String[] asVariantArray(Class e) {
		temporaryVariantsEnum = e;
		temporaryVariantProp = PropertyEnum.create(IVariantEnumHolder.HEADER, e);
		Enum[] values = (Enum[]) e.getEnumConstants();
		String[] variants = new String[values.length];
		
		for(int i = 0; i < values.length; i++)
			variants[i] = values[i].name().toLowerCase();
		return variants;
	}
	
	public static interface EnumBase extends IStringSerializable {

		@Override
		public default String getName() {
			return ((Enum) this).name().toLowerCase();
		}
		
	}

}
