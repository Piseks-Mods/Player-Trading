package cz.pisekpiskovec.playertrading.procedures;

import net.minecraft.world.World;
import net.minecraft.world.IWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.Entity;
import net.minecraft.block.Blocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Block;

import java.util.Map;

import cz.pisekpiskovec.playertrading.PiseksPlayerTradingMod;

public class BoothPlacedByProcedure {

	public static void executeProcedure(Map<String, Object> dependencies) {
		if (dependencies.get("world") == null) {
			if (!dependencies.containsKey("world"))
				PiseksPlayerTradingMod.LOGGER.warn("Failed to load dependency world for procedure BoothPlacedBy!");
			return;
		}
		if (dependencies.get("x") == null) {
			if (!dependencies.containsKey("x"))
				PiseksPlayerTradingMod.LOGGER.warn("Failed to load dependency x for procedure BoothPlacedBy!");
			return;
		}
		if (dependencies.get("y") == null) {
			if (!dependencies.containsKey("y"))
				PiseksPlayerTradingMod.LOGGER.warn("Failed to load dependency y for procedure BoothPlacedBy!");
			return;
		}
		if (dependencies.get("z") == null) {
			if (!dependencies.containsKey("z"))
				PiseksPlayerTradingMod.LOGGER.warn("Failed to load dependency z for procedure BoothPlacedBy!");
			return;
		}
		if (dependencies.get("entity") == null) {
			if (!dependencies.containsKey("entity"))
				PiseksPlayerTradingMod.LOGGER.warn("Failed to load dependency entity for procedure BoothPlacedBy!");
			return;
		}
		IWorld world = (IWorld) dependencies.get("world");
		double x = dependencies.get("x") instanceof Integer ? (int) dependencies.get("x") : (double) dependencies.get("x");
		double y = dependencies.get("y") instanceof Integer ? (int) dependencies.get("y") : (double) dependencies.get("y");
		double z = dependencies.get("z") instanceof Integer ? (int) dependencies.get("z") : (double) dependencies.get("z");
		Entity entity = (Entity) dependencies.get("entity");
		BlockState getBlock = Blocks.AIR.getDefaultState();
		String oldOwner = "";
		if (!world.isRemote()) {
			BlockPos _bp = new BlockPos(x, y, z);
			TileEntity _tileEntity = world.getTileEntity(_bp);
			BlockState _bs = world.getBlockState(_bp);
			if (_tileEntity != null)
				_tileEntity.getTileData().putString("boothOwner", (entity.getDisplayName().getString()));
			if (world instanceof World)
				((World) world).notifyBlockUpdate(_bp, _bs, _bs, 3);
		}
		if (!world.isAirBlock(new BlockPos(x, y - 1, z)) && world.isAirBlock(new BlockPos(x, y + 2, z))) {
			getBlock = (world.getBlockState(new BlockPos(x, y, z)));
			oldOwner = (new Object() {
				public String getValue(IWorld world, BlockPos pos, String tag) {
					TileEntity tileEntity = world.getTileEntity(pos);
					if (tileEntity != null)
						return tileEntity.getTileData().getString(tag);
					return "";
				}
			}.getValue(world, new BlockPos(x, y, z), "boothOwner"));
			world.setBlockState(new BlockPos(x, y, z), Blocks.AIR.getDefaultState(), 3);
			world.setBlockState(new BlockPos(x, y + 1, z), (getBlock), 3);
			if (!world.isRemote()) {
				BlockPos _bp = new BlockPos(x, y + 1, z);
				TileEntity _tileEntity = world.getTileEntity(_bp);
				BlockState _bs = world.getBlockState(_bp);
				if (_tileEntity != null)
					_tileEntity.getTileData().putString("boothOwner", oldOwner);
				if (world instanceof World)
					((World) world).notifyBlockUpdate(_bp, _bs, _bs, 3);
			}
		} else {
			if (world instanceof World) {
				Block.spawnDrops(world.getBlockState(new BlockPos(x, y, z)), (World) world, new BlockPos(x + 0.5, y + 0.5, z + 0.5));
				world.destroyBlock(new BlockPos(x, y, z), false);
			}
		}
	}
}
