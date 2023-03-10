
package cz.pisekpiskovec.playertrading.block;

import net.minecraftforge.registries.ObjectHolder;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.World;
import net.minecraft.world.IBlockReader;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.Rotation;
import net.minecraft.util.Mirror;
import net.minecraft.util.Direction;
import net.minecraft.state.StateContainer;
import net.minecraft.state.DirectionProperty;
import net.minecraft.loot.LootContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.BlockItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.SoundType;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Block;

import java.util.stream.Stream;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.Collections;
import java.util.AbstractMap;

import cz.pisekpiskovec.playertrading.procedures.BoothPlacementProcedure;
import cz.pisekpiskovec.playertrading.PiseksPlayerTradingModElements;

@PiseksPlayerTradingModElements.ModElement.Tag
public class BoothCrimsonWarpedBlock extends PiseksPlayerTradingModElements.ModElement {
	@ObjectHolder("piseks_player_trading:booth_crimson_warped")
	public static final Block block = null;

	public BoothCrimsonWarpedBlock(PiseksPlayerTradingModElements instance) {
		super(instance, 1);
	}

	@Override
	public void initElements() {
		elements.blocks.add(() -> new CustomBlock());
		elements.items.add(() -> new BlockItem(block, new Item.Properties().group(ItemGroup.MISC)).setRegistryName(block.getRegistryName()));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void clientLoad(FMLClientSetupEvent event) {
		RenderTypeLookup.setRenderLayer(block, RenderType.getCutout());
	}

	public static class CustomBlock extends Block {
		public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;

		public CustomBlock() {
			super(Block.Properties.create(Material.WOOD, MaterialColor.WOOD).sound(SoundType.WOOD).hardnessAndResistance(13.625f, 18.625f)
					.setLightLevel(s -> 0).harvestLevel(0).harvestTool(ToolType.AXE).notSolid().setOpaque((bs, br, bp) -> false));
			this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH));
			setRegistryName("booth_crimson_warped");
		}

		@Override
		public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
			return true;
		}

		@Override
		public int getOpacity(BlockState state, IBlockReader worldIn, BlockPos pos) {
			return 0;
		}

		@Override
		public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
			Vector3d offset = state.getOffset(world, pos);
			switch ((Direction) state.get(FACING)) {
				case SOUTH :
				default :
					return VoxelShapes
							.or(makeCuboidShape(16, -16, 16, 0, 0, 0), makeCuboidShape(16, -16, 10, 20, 30, 6),
									makeCuboidShape(0, -16, 10, -4, 30, 6), makeCuboidShape(16, 30, 16, 0, 32, 0))

							.withOffset(offset.x, offset.y, offset.z);
				case NORTH :
					return VoxelShapes
							.or(makeCuboidShape(0, -16, 0, 16, 0, 16), makeCuboidShape(0, -16, 6, -4, 30, 10),
									makeCuboidShape(16, -16, 6, 20, 30, 10), makeCuboidShape(0, 30, 0, 16, 32, 16))

							.withOffset(offset.x, offset.y, offset.z);
				case EAST :
					return VoxelShapes
							.or(makeCuboidShape(16, -16, 0, 0, 0, 16), makeCuboidShape(10, -16, 0, 6, 30, -4),
									makeCuboidShape(10, -16, 16, 6, 30, 20), makeCuboidShape(16, 30, 0, 0, 32, 16))

							.withOffset(offset.x, offset.y, offset.z);
				case WEST :
					return VoxelShapes
							.or(makeCuboidShape(0, -16, 16, 16, 0, 0), makeCuboidShape(6, -16, 16, 10, 30, 20),
									makeCuboidShape(6, -16, 0, 10, 30, -4), makeCuboidShape(0, 30, 16, 16, 32, 0))

							.withOffset(offset.x, offset.y, offset.z);
			}
		}

		@Override
		protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
			builder.add(FACING);
		}

		@Override
		public BlockState getStateForPlacement(BlockItemUseContext context) {
			return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
		}

		public BlockState rotate(BlockState state, Rotation rot) {
			return state.with(FACING, rot.rotate(state.get(FACING)));
		}

		public BlockState mirror(BlockState state, Mirror mirrorIn) {
			return state.rotate(mirrorIn.toRotation(state.get(FACING)));
		}

		@Override
		public MaterialColor getMaterialColor() {
			return MaterialColor.WOOD;
		}

		@Override
		public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
			List<ItemStack> dropsOriginal = super.getDrops(state, builder);
			if (!dropsOriginal.isEmpty())
				return dropsOriginal;
			return Collections.singletonList(new ItemStack(this, 1));
		}

		@Override
		public void onBlockPlacedBy(World world, BlockPos pos, BlockState blockstate, LivingEntity entity, ItemStack itemstack) {
			super.onBlockPlacedBy(world, pos, blockstate, entity, itemstack);
			int x = pos.getX();
			int y = pos.getY();
			int z = pos.getZ();

			BoothPlacementProcedure.executeProcedure(Stream
					.of(new AbstractMap.SimpleEntry<>("world", world), new AbstractMap.SimpleEntry<>("x", x), new AbstractMap.SimpleEntry<>("y", y),
							new AbstractMap.SimpleEntry<>("z", z))
					.collect(HashMap::new, (_m, _e) -> _m.put(_e.getKey(), _e.getValue()), Map::putAll));
		}
	}
}
