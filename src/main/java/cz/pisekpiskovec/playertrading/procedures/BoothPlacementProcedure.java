package cz.pisekpiskovec.playertrading.procedures;

import net.minecraft.world.IWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.Blocks;
import net.minecraft.block.BlockState;

import java.util.Map;

import cz.pisekpiskovec.playertrading.PiseksPlayerTradingMod;

public class BoothPlacementProcedure {

	public static void executeProcedure(Map<String, Object> dependencies) {
		if (dependencies.get("world") == null) {
			if (!dependencies.containsKey("world"))
				PiseksPlayerTradingMod.LOGGER.warn("Failed to load dependency world for procedure BoothPlacement!");
			return;
		}
		if (dependencies.get("x") == null) {
			if (!dependencies.containsKey("x"))
				PiseksPlayerTradingMod.LOGGER.warn("Failed to load dependency x for procedure BoothPlacement!");
			return;
		}
		if (dependencies.get("y") == null) {
			if (!dependencies.containsKey("y"))
				PiseksPlayerTradingMod.LOGGER.warn("Failed to load dependency y for procedure BoothPlacement!");
			return;
		}
		if (dependencies.get("z") == null) {
			if (!dependencies.containsKey("z"))
				PiseksPlayerTradingMod.LOGGER.warn("Failed to load dependency z for procedure BoothPlacement!");
			return;
		}
		IWorld world = (IWorld) dependencies.get("world");
		double x = dependencies.get("x") instanceof Integer ? (int) dependencies.get("x") : (double) dependencies.get("x");
		double y = dependencies.get("y") instanceof Integer ? (int) dependencies.get("y") : (double) dependencies.get("y");
		double z = dependencies.get("z") instanceof Integer ? (int) dependencies.get("z") : (double) dependencies.get("z");
		BlockState getBlock = Blocks.AIR.getDefaultState();
		if (!world.isAirBlock(new BlockPos(x, y + -1, z))) {
			getBlock = (world.getBlockState(new BlockPos(x, y, z)));
			world.setBlockState(new BlockPos(x, y, z), Blocks.AIR.getDefaultState(), 3);
			world.setBlockState(new BlockPos(x, y + 1, z), (getBlock), 3);
		}
	}
}
