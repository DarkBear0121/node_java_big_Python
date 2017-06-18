/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [16/01/2016, 22:57:27 (GMT)]
 */
package vazkii.psi.common.spell.other;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.psi.api.spell.EnumPieceType;
import vazkii.psi.api.spell.IRedirector;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamAny;
import vazkii.psi.common.lib.LibResources;

public class PieceConnector extends SpellPiece implements IRedirector {

	private static final ResourceLocation lines = new ResourceLocation(LibResources.SPELL_CONNECTOR_LINES);

	public SpellParam target;

	public PieceConnector(Spell spell) {
		super(spell);
	}

	@Override
	public String getSortingName() {
		return "00000000000";
	}

	@Override
	public String getEvaluationTypeString() {
		return I18n.translateToLocal("psi.datatype.Any");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void drawAdditional() {
		drawSide(paramSides.get(target));

		if(isInGrid)
			for(SpellParam.Side side : SpellParam.Side.class.getEnumConstants())
				if(side.isEnabled()) {
					SpellPiece piece = spell.grid.getPieceAtSideSafely(x, y, side);
					if(piece != null)
						for(SpellParam param : piece.paramSides.keySet()) {
							SpellParam.Side paramSide = piece.paramSides.get(param);
							if(paramSide.getOpposite() == side) {
								drawSide(side);
								break;
							}
						}
				}
	}

	@SideOnly(Side.CLIENT)
	public void drawSide(SpellParam.Side side) {
		if(side.isEnabled()) {
			Minecraft mc = Minecraft.getMinecraft();
			mc.renderEngine.bindTexture(lines);

			double minU = 0;
			double minV = 0;
			switch(side) {
			case LEFT:
				minU = 0.5;
				break;
			case RIGHT: break;
			case TOP:
				minV = 0.5;
				break;
			case BOTTOM:
				minU = 0.5;
				minV = 0.5;
				break;
			default: break;
			}

			double maxU = minU + 0.5;
			double maxV = minV + 0.5;

			GlStateManager.color(1F, 1F, 1F);
			BufferBuilder wr = Tessellator.getInstance().getBuffer();
			wr.begin(7, DefaultVertexFormats.POSITION_TEX);
			wr.pos(0, 16, 0).tex(minU, maxV).endVertex();
			wr.pos(16, 16, 0).tex(maxU, maxV).endVertex();
			wr.pos(16, 0, 0).tex(maxU, minV).endVertex();;
			wr.pos(0, 0, 0).tex(minU, minV).endVertex();
			Tessellator.getInstance().draw();
		}
	}

	@Override
	public void getShownPieces(List<SpellPiece> pieces) {
		for(SpellParam.Side side : SpellParam.Side.class.getEnumConstants())
			if(side.isEnabled()) {
				PieceConnector piece = (PieceConnector) copy();
				piece.paramSides.put(piece.target, side);
				pieces.add(piece);
			}
	}

	@Override
	public void initParams() {
		addParam(target = new ParamAny(SpellParam.GENERIC_NAME_TARGET, SpellParam.GRAY, false));
	}

	@Override
	public EnumPieceType getPieceType() {
		return EnumPieceType.CONNECTOR;
	}

	@Override
	public SpellParam.Side getRedirectionSide() {
		return paramSides.get(target);
	}

	// Side this class implements IRedirector we don't need this
	@Override
	public Class<?> getEvaluationType() {
		return null;
	}

	@Override
	public Object evaluate() {
		return null;
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		return null;
	}


}
