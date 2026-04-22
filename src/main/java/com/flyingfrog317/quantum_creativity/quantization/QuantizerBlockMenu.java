package com.flyingfrog317.quantum_creativity.quantization;

import com.flyingfrog317.quantum_creativity.QuantumCreativity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

class QuantizerBlockMenu extends AbstractContainerMenu {
    private final ContainerLevelAccess access;
    private static Tuple<IItemHandler,ContainerLevelAccess> construction_helper (Inventory inv, FriendlyByteBuf buf){
        BlockPos pos = buf.readBlockPos();
        BlockEntity be = inv.player.level().getBlockEntity(pos);
        LazyOptional<IItemHandler> handler = Objects.requireNonNull(be)
                .getCapability(ForgeCapabilities.ITEM_HANDLER, null);
        IItemHandler item = handler.orElseThrow(() -> new IllegalStateException("Missing ItemHandler capability!"));
        ContainerLevelAccess access = ContainerLevelAccess.create(inv.player.level(), pos);
        return new Tuple<>(item,access);
    }
    public QuantizerBlockMenu(int id, Inventory inv, FriendlyByteBuf buf) {
        this(
                id,
                inv,
                construction_helper(inv,buf)
        );
        QuantumCreativity.LOGGER.info("buf");

    }
    public QuantizerBlockMenu(int pContainerId, Inventory playerInventory, IItemHandler blockInventory, ContainerLevelAccess access){
        this(
                pContainerId,
                playerInventory,
                new Tuple<>(blockInventory,access)
        );
    }
    public QuantizerBlockMenu(int pContainerId, Inventory playerInventory, Tuple<IItemHandler,ContainerLevelAccess> tuple){
        super(QuantizingRegistries.QuantizerBlockMenuReg.get(), pContainerId);
        QuantumCreativity.LOGGER.info("const");
        IItemHandler blockInventory = tuple.getA();
        this.access = tuple.getB();
        int centerX = 80;
        int centerY = 69;

// Add the central slot
        this.addSlot(new SlotItemHandler(blockInventory, 0, centerX, centerY));

// Relative offsets for all other slots
        int[][] offsets = {
                {0, -51},    // Slot above center
                {26, -45},   // Slot 2
                {44, -25},   // Slot 3
                {51, 0},    // Slot 4
                {44, 25},    // Slot 5
                {26, 45},    // Slot 6
                {0, 51},    // Slot 7
                {-26, 45},   // Slot 8
                {-44, 25},   // Slot 9
                {-51, 0},   // Slot 10
                {-44, -25},  // Slot 11
                {-26, -45}   // Slot 12
        };

// Loop through all offsets and add as ResultSlotItemHandler
        for (int i = 0; i < offsets.length; i++) {
            int x = centerX + offsets[i][0];
            int y = centerY + offsets[i][1];
            this.addSlot(new ResultSlotItemHandler(blockInventory, i + 1, x, y));
        }
        // ====================== PLAYER INVENTORY ======================

        // Player main inventory (3 rows × 9)
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInventory,
                        col + row * 9 + 9,
                        8 + col * 18,
                        158 + row * 18));   // starts below the ring
            }
        }

        // Player hotbar
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 216));
        }
        QuantumCreativity.LOGGER.info("ended");

    }
    static class ResultSlotItemHandler extends SlotItemHandler {
        public ResultSlotItemHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
        public boolean mayPlace(@NotNull ItemStack stack) {
            return false;
        }
    }
    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        ItemStack moved=ItemStack.EMPTY;
        Slot slot = this.slots.get(i);
        if (!slot.hasItem()){
            return moved;
        }
        ItemStack inSlot = slot.getItem();
        moved = inSlot.copy();
        int machineSlots=13;
        if (i < machineSlots){
            if (!this.moveItemStackTo(inSlot,machineSlots+27,this.slots.size(),false) && !this.moveItemStackTo(inSlot,machineSlots,this.slots.size()-9,false)){
                return ItemStack.EMPTY;
            }
        } else {
            if (!this.moveItemStackTo(inSlot,0,1,false)){
                return ItemStack.EMPTY;
            }
        }
        if (inSlot.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        slot.onTake(player, inSlot);

        return moved;
    }
    @Override
    public boolean stillValid(Player player) {
        return AbstractContainerMenu.stillValid(this.access,player,QuantizingRegistries.QuantizerBlockReg.get());
    }
}
