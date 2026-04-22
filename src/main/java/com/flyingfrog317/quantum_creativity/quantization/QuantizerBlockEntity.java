package com.flyingfrog317.quantum_creativity.quantization;

import com.flyingfrog317.quantum_creativity.QuantumCreativity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.Color;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Objects;
import java.util.Optional;

@Mod.EventBusSubscriber(modid = QuantumCreativity.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class QuantizerBlockEntity extends BlockEntity implements MenuProvider, WorldlyContainer, GeoBlockEntity {

    private final ItemStackHandler inventory = new ItemStackHandler(13);

    private LazyOptional<IItemHandlerModifiable>[] handlers =
            SidedInvWrapper.create(this, Direction.values());

    public QuantizerBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(QuantizingRegistries.QuantizerBlockEntityReg.get(), pPos, pBlockState);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("inventory", inventory.serializeNBT());
        tag.putInt("crafting_progress", craftingProgress);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        inventory.deserializeNBT(tag.getCompound("inventory"));
        craftingProgress = tag.getInt("crafting_progress");
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        load(tag);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        CompoundTag tag = pkt.getTag();
        if (tag != null) {
            load(tag);
        }
    }

    // =========================
    // Capability handling
    // =========================

    @Override
    public void onLoad() {
        super.onLoad();
        handlers = SidedInvWrapper.create(this, Direction.values());
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        for (LazyOptional<IItemHandlerModifiable> handler : handlers) {
            handler.invalidate();
        }
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {

        if (cap == ForgeCapabilities.ITEM_HANDLER) {

            if (side == null) {
                return LazyOptional.of(() -> inventory).cast();
            }

            return handlers[side.ordinal()].cast();
        }

        return super.getCapability(cap, side);
    }

    // =========================
    // Sided inventory rules
    // =========================

    @Override
    public int[] getSlotsForFace(Direction direction) {
        if (direction == Direction.UP) {
            return new int[]{0}; // input slot
        } else {
            return new int[]{1,2,3,4,5,6,7,8,9,10,11,12}; // output slots
        }
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction direction) {
        return direction == Direction.UP && slot == 0;
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction direction) {
        return direction != Direction.UP && slot != 0;
    }

    // =========================
    // Container implementation
    // =========================

    @Override
    public int getContainerSize() {
        return inventory.getSlots();
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < inventory.getSlots(); i++) {
            if (!inventory.getStackInSlot(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return inventory.getStackInSlot(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack extracted = inventory.extractItem(slot, amount, false);
        if (!extracted.isEmpty()) {
            setChanged();
        }
        return extracted;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack stack = inventory.getStackInSlot(slot);
        inventory.setStackInSlot(slot, ItemStack.EMPTY);
        return stack;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        inventory.setStackInSlot(slot, stack);
        setChanged();
        syncToClient();
    }

    @Override
    public boolean stillValid(Player player) {
        if (level == null || level.getBlockEntity(worldPosition) != this) return false;

        return player.distanceToSqr(
                worldPosition.getX() + 0.5,
                worldPosition.getY() + 0.5,
                worldPosition.getZ() + 0.5
        ) <= 64;
    }

    @Override
    public void clearContent() {
        for (int i = 0; i < inventory.getSlots(); i++) {
            inventory.setStackInSlot(i, ItemStack.EMPTY);
        }
        setChanged();
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.quantum_creativity.quantizer");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {

        LazyOptional<IItemHandler> handler =
                this.getCapability(ForgeCapabilities.ITEM_HANDLER, null);

        IItemHandler itemHandler = handler.orElseThrow(
                () -> new IllegalStateException("Missing ITEM_HANDLER capability")
        );
        //noinspection DataFlowIssue
        return new QuantizerBlockMenu(
                id,
                inv,
                itemHandler,
                ContainerLevelAccess.create(this.level, this.getBlockPos())
        );
    }
    private static final float IDLE_ANIM_SPEED = 0.2f;
    private static final float ACTIVE_ANIM_SPEED = 1.0f;

    private float animSpeed = IDLE_ANIM_SPEED;

    protected <E extends QuantizerBlockEntity> PlayState animController(final AnimationState<E> state){
        AnimationController<?> controller=state.getController();
        if (controller.getCurrentAnimation()==null){
            controller.setAnimation(RawAnimation.begin().then("rotate", Animation.LoopType.LOOP));
        }
        controller.setAnimationSpeed(this.animSpeed);
        return PlayState.CONTINUE;
    }
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this,this::animController));
    }
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
    static class QuantizerModel extends GeoModel<QuantizerBlockEntity> {
        @Override
        public ResourceLocation getModelResource(QuantizerBlockEntity QuantizerBlockEntity) {
            return QuantumCreativity.asResource(QuantizerBlockEntity.getModelPath());
        }

        @Override
        public ResourceLocation getTextureResource(QuantizerBlockEntity QuantizerBlockEntity) {
            return QuantumCreativity.asResource(QuantizerBlockEntity.getTexturePath());
        }

        @Override
        public ResourceLocation getAnimationResource(QuantizerBlockEntity QuantizerBlockEntity) {
            return QuantumCreativity.asResource(QuantizerBlockEntity.getAnimationPath());
        }
    }
    static class Renderer extends GeoBlockRenderer<QuantizerBlockEntity> {
        public Renderer(BlockEntityRendererProvider.Context ctx) {
            super(new QuantizerModel());
        }

        @Override
        public Color getRenderColor(QuantizerBlockEntity animatable, float partialTick, int packedLight) {
            return new Color(0xffffffff);
        }

        @Override
        public RenderType getRenderType(QuantizerBlockEntity animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
            return RenderType.entityTranslucentEmissive(texture);
        }

    }
    private String getModelPath() {
        return "geo/quantizer.geo.json";
    }
    private String getTexturePath() {
        return "textures/block/quantizer.png";
    }
    private String getAnimationPath() {
        return "animations/quantizer.animation.json";
    }
    @SubscribeEvent
    public static void registerRenderer(final EntityRenderersEvent.RegisterRenderers event){
        event.registerBlockEntityRenderer(QuantizingRegistries.QuantizerBlockEntityReg.get(), Renderer::new);
    }
    private final int craftingTicks = 100;
    private int craftingProgress = 0;
    @SuppressWarnings("unused")
    public static void tick(Level level, BlockPos pos, BlockState state, QuantizerBlockEntity blockEntity){
        if (level.isClientSide()){
            if (!blockEntity.getItem(0).isEmpty()){
                blockEntity.animSpeed = ACTIVE_ANIM_SPEED;
            } else {
                blockEntity.animSpeed = IDLE_ANIM_SPEED;
            }
        } else {
            int previousProgress = blockEntity.craftingProgress;
            boolean hadInput = !blockEntity.getItem(0).isEmpty();
            if (!blockEntity.getItem(0).isEmpty()){
                blockEntity.craftingProgress++;
                if (blockEntity.craftingProgress>=blockEntity.craftingTicks){
                    if (blockEntity.completeCraft()) blockEntity.craftingProgress=0;
                }
                blockEntity.setChanged();
            } else {
                blockEntity.craftingProgress=0;
            }

            boolean hasInput = !blockEntity.getItem(0).isEmpty();
            if (hadInput != hasInput || previousProgress != blockEntity.craftingProgress) {
                blockEntity.syncToClient();
            }
        }
    }

    private void syncToClient() {
        if (level == null || level.isClientSide()) {
            return;
        }

        setChanged();
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
    }

    private ItemStack cachedOutput;
    private boolean completeCraft(){
        if (cachedOutput!=null){
            if(outputItem(cachedOutput)){
                cachedOutput=null;
                return true;
            }
            return false;
        }
        SimpleContainer container=new SimpleContainer(getItem(0));

        Optional<QuantizingRecipe> recipe=Objects.requireNonNull(level).getRecipeManager().getRecipeFor(QuantizingRegistries.QuantizingRecipeType.get(),container,level);
        if (recipe.isPresent()){
            ItemStack output = recipe.get().resolve(level.random);
            if (outputItem(output)) {
                getItem(0).shrink(1);
            } else {
                cachedOutput=output.copy();
                return false;
            }
        }
        return true;
    }
    private boolean outputItem(ItemStack output){
        for (int i=1; i<inventory.getSlots();i++){
            ItemStack slot = getItem(i);
            if (ItemStack.isSameItem(output, slot) && slot.getCount() + output.getCount() <= slot.getMaxStackSize()){
                slot.grow(output.getCount());
                setItem(i,slot);
                return true;
            } else if (slot.isEmpty()) {
                setItem(i,output);
                return true;
            }
        }
        return false;
    }
}
