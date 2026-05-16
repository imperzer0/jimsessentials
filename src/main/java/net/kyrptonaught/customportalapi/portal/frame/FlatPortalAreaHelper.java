package net.kyrptonaught.customportalapi.portal.frame;

import com.google.common.collect.Sets;
import net.minecraft.BlockUtil;
import net.minecraft.BlockUtil.FoundRectangle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;
import java.util.function.Predicate;

import net.kyrptonaught.customportalapi.CustomPortalApiRegistry;
import net.kyrptonaught.customportalapi.CustomPortalsMod;
import net.kyrptonaught.customportalapi.util.CustomPortalHelper;
import net.kyrptonaught.customportalapi.util.PortalLink;

public class FlatPortalAreaHelper extends PortalFrameTester {

    protected final int maxXSize = 21, maxZSize = 21;

    protected int xSize = -1, zSize = -1;

    public FlatPortalAreaHelper() {}

    public FlatPortalAreaHelper init(LevelAccessor world, BlockPos blockPos, Axis axis, Block... foundations) {
        VALID_FRAME = Sets.newHashSet(foundations);
        this.world = world;
        this.lowerCorner = this.getLowerCorner(blockPos, Axis.X, Axis.Z);
        this.foundPortalBlocks = 0;
        if (lowerCorner == null) {
            lowerCorner = blockPos;
            xSize = zSize = 1;
        } else {
            this.xSize = this.getSize(Axis.X, 2, maxXSize);
            if (this.xSize > 0) {
                this.zSize = this.getSize(Axis.Z, 2, maxZSize);
                if (checkForValidFrame(Axis.X, Axis.Z, xSize, zSize)) {
                    countExistingPortalBlocks(Axis.X, Axis.Z, xSize, zSize);
                } else {
                    lowerCorner = null;
                    xSize = zSize = 1;
                }
            }
        }
        return this;
    }

    public Optional<PortalFrameTester> getNewPortal(
        LevelAccessor worldAccess,
        BlockPos blockPos,
        Axis axis,
        Block... foundations
    ) {
        return getOrEmpty(
            worldAccess,
            blockPos,
            areaHelper -> areaHelper.isValidFrame() && areaHelper.foundPortalBlocks == 0,
            axis,
            foundations
        );
    }

    public Optional<PortalFrameTester> getOrEmpty(
        LevelAccessor worldAccess,
        BlockPos blockPos,
        Predicate<PortalFrameTester> predicate,
        Axis axis,
        Block... foundations
    ) {
        return Optional.of(
            (PortalFrameTester) new FlatPortalAreaHelper().init(worldAccess, blockPos, axis, foundations)
        )
            .filter(
                predicate
            );
    }

    public boolean isAlreadyLitPortalFrame() {
        return this.isValidFrame() && this.foundPortalBlocks == this.xSize * this.zSize;
    }

    public boolean isValidFrame() {
        return this.lowerCorner != null && xSize >= 2 && zSize >= 2 && xSize < maxXSize && zSize < maxZSize;
    }

    public void lightPortal(Block frameBlock) {
        PortalLink link = CustomPortalApiRegistry.getPortalLinkFromBase(frameBlock);
        BlockState blockState = CustomPortalHelper.blockWithAxis(
            link != null ? link.getPortalBlock().defaultBlockState() : CustomPortalsMod.getDefaultPortalBlock().defaultBlockState(),
            Axis.Y
        );
        BlockPos.betweenClosed(
            this.lowerCorner,
            this.lowerCorner.relative(Axis.X, this.xSize - 1)
                .relative(
                    Axis.Z,
                    this.zSize - 1
                )
        ).forEach(blockPos -> this.world.setBlock(blockPos, blockState, 18));
    }

    @Override
    public void createPortal(Level world, BlockPos pos, BlockState frameBlock, Axis axis) {
        for (int i = -1; i < 3; i++) {
            world.setBlockAndUpdate(pos.relative(Axis.X, i).relative(Axis.Z, -1), frameBlock);
            world.setBlockAndUpdate(pos.relative(Axis.X, i).relative(Axis.Z, 2), frameBlock);

            world.setBlockAndUpdate(pos.relative(Axis.Z, i).relative(Axis.X, -1), frameBlock);
            world.setBlockAndUpdate(pos.relative(Axis.Z, i).relative(Axis.X, 2), frameBlock);
        }
        for (int i = 0; i < 2; i++) {
            placeLandingPad(world, pos.relative(Axis.X, i).below(), frameBlock);
            placeLandingPad(world, pos.relative(Axis.X, i).relative(Axis.Z, 1).below(), frameBlock);

            fillAirAroundPortal(world, pos.relative(Axis.X, i).above());
            fillAirAroundPortal(world, pos.relative(Axis.X, i).relative(Axis.Z, 1).above());
            fillAirAroundPortal(world, pos.relative(Axis.X, i).above(2));
            fillAirAroundPortal(world, pos.relative(Axis.X, i).relative(Axis.Z, 1).above(2));
        }
        // inits this instance based off of the newly created portal;
        this.lowerCorner = pos;
        this.xSize = zSize = 2;
        this.world = world;
        this.foundPortalBlocks = 4;
        lightPortal(frameBlock.getBlock());
    }

    protected void fillAirAroundPortal(Level world, BlockPos pos) {
        if (world.getBlockState(pos).isSolid())
            world.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_KNOWN_SHAPE);
    }

    protected void placeLandingPad(Level world, BlockPos pos, BlockState frameBlock) {
        if (!world.getBlockState(pos).isSolid())
            world.setBlockAndUpdate(pos, frameBlock);
    }

    @Override
    public boolean isRequestedSize(int attemptWidth, int attemptHeight) {
        return ((xSize == attemptWidth || attemptHeight == 0) && (zSize == attemptHeight) || attemptWidth == 0) || ((xSize == attemptHeight
            || attemptHeight == 0) && (zSize == attemptWidth || attemptWidth == 0));
    }

    @Override
    public FoundRectangle getRectangle() {
        return new FoundRectangle(lowerCorner, xSize, zSize);
    }

    @Override
    public Axis getAxis1() {
        return Axis.X;
    }

    @Override
    public Axis getAxis2() {
        return Axis.Z;
    }

    @Override
    public BlockPos doesPortalFitAt(Level world, BlockPos attemptPos, Axis axis) {
        FoundRectangle rect = BlockUtil.getLargestRectangleAround(
            attemptPos.above(),
            Axis.X,
            4,
            Axis.Z,
            4,
            blockPos -> world.getBlockState(blockPos).isSolid() && !world.getBlockState(
                blockPos.above()
            ).isSolid() && !world.getBlockState(
                blockPos.above()
            ).liquid() && !world.getBlockState(
                blockPos.above(2)
            ).isSolid() && !world.getBlockState(blockPos.above(2)).liquid()
        );
        return rect.axis1Size >= 4 && rect.axis2Size >= 4 ? rect.minCorner : null;
    }

    @Override
    public Vec3 getEntityOffsetInPortal(FoundRectangle arg, Entity entity, Axis portalAxis) {
        EntityDimensions entityDimensions = entity.getDimensions(entity.getPose());
        double xSize = arg.axis1Size - entityDimensions.width();
        double zSize = arg.axis2Size - entityDimensions.width();

        double deltaX = Mth.inverseLerp(entity.getX(), arg.minCorner.getX(), arg.minCorner.getX() + xSize);
        double deltaY = Mth.inverseLerp(entity.getY(), arg.minCorner.getY() - 1D, arg.minCorner.getY() + 1D);
        double deltaZ = Mth.inverseLerp(entity.getZ(), arg.minCorner.getZ(), arg.minCorner.getZ() + zSize);

        return new Vec3(deltaX, deltaY, deltaZ);
    }

    @Override
    public DimensionTransition getTPTargetInPortal(
        ServerLevel world,
        FoundRectangle portalRect,
        Axis portalAxis,
        Vec3 prevOffset,
        Entity entity,
        PortalLink link
    ) {
        var entityDimensions = entity.getDimensions(entity.getPose());
        var xSize = portalRect.axis1Size - entityDimensions.width();
        var zSize = portalRect.axis2Size - entityDimensions.width();

        var x = Mth.lerp(prevOffset.x, portalRect.minCorner.getX(), portalRect.minCorner.getX() + xSize);
        var z = Mth.lerp(prevOffset.z, portalRect.minCorner.getZ(), portalRect.minCorner.getZ() + zSize);

        DimensionTransition.PostDimensionTransition post = DimensionTransition.PLAY_PORTAL_SOUND.then(entityx -> {
            entityx.placePortalTicket(portalRect.minCorner);
            link.executePostTPEvent(entityx);
        });
        return new DimensionTransition(
            world,
            new Vec3(x, portalRect.minCorner.getY() + 1D, z),
            entity.getDeltaMovement(),
            entity.getYRot(),
            entity.getXRot(),
            post
        );
    }
}
