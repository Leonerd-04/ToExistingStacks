package Ieonerd04.ToExistingStacks.mixin;

import Ieonerd04.ToExistingStacks.ToExistingStacks;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;

@Mixin(HandledScreen.class)
public class MixinHandledScreen extends Screen {

	protected MixinHandledScreen(Text title){
		super(title);
	}

	@Inject(method = "keyPressed", at = @At("TAIL"))
	private void onKeyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir){
		if(ToExistingStacks.MOVE_TO_CONTAINER.matchesKey(keyCode, scanCode)){
			onPressUp();
		}
	}

	//Transfers the items in a players inventory to a container that already have stacks inside them
	private void onPressUp(){
		ScreenHandler handler = client.player.currentScreenHandler;

		// This list tracks the types of items within the container
		// The items' translation keys will act as their identifiers to determine their type
		ArrayList<String> containerItems = new ArrayList<>();

		// The slots handled by the ScreenHandler include both the container and inventory, in that order,
		// So we have to account for that to separate the inventories
		int numContainerSlots = handler.slots.size() - client.player.getInventory().main.size();

		// Determines what types of items are in the container
		for(int i = 0; i < numContainerSlots; i++){
			String item = handler.getSlot(i).getStack().getItem().getTranslationKey();
			if(containerItems.contains(item) || item.equals(Items.AIR.getTranslationKey())) continue;

			containerItems.add(item);
		}

		// Loops through the player's inventory slots and determines which ones contain
		// items that could be transferred to a particular container.
		// Then shift-clicks them (basically)
		for(int i = numContainerSlots; i < handler.slots.size(); i++){
			if(containerItems.contains(handler.getSlot(i).getStack().getItem().getTranslationKey())){
				//The shift-click call is made to the ClientPlayerInteractionManager for proper server-client communication
				client.interactionManager.clickSlot(handler.syncId, i, 0, SlotActionType.QUICK_MOVE, client.player);
			}
		}

	}
}
