package Ieonerd04.ToExistingStacks.mixin;

import Ieonerd04.ToExistingStacks.ToExistingStacks;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.MerchantScreen;
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

	// The code for pressing the keybinding is injected into the keyPressed method of the HandledScreen class
	// rather than being handled by the Fabric Api's ClientLifecycleEvents class
	// because that doesn't work whenever the client isn't ticking (ie. in an inventory screen)
	@Inject(method = "keyPressed", at = @At("TAIL"))
	private void onKeyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir){
		if(ToExistingStacks.MOVE_TO_CONTAINER.matchesKey(keyCode, scanCode)){
			addToExistingStacks();
		}
	}

	private void addToExistingStacks(){
		// Suppress NullPointerException warnings
		if(client == null) return;
		if(client.player == null) return;

		Screen screen = client.currentScreen;

		// Prevents the keybinding from working on screens where the mod doesn't do anything.
		// There are some other situations where the result isn't too useful imo, like in the crafting tables,
		// but I've left that for the end user to decide for themselves how useful it is.
		if(screen instanceof AbstractInventoryScreen || screen instanceof MerchantScreen){
			return;
		}

		ScreenHandler handler = client.player.currentScreenHandler;

		// Items' translation keys will act as their identifiers to determine their type
		ArrayList<String> containerItems = new ArrayList<>();

		// The slots handled by the ScreenHandler include both the container and inventory, in that order,
		// so we have to account for that to separate the inventories
		int numContainerSlots = handler.slots.size() - client.player.getInventory().main.size();

		// Determines what types of items are in the container
		for(int i = 0; i < numContainerSlots; i++){
			String item = handler.getSlot(i).getStack().getItem().getTranslationKey();

			// We want only one entry per type of item, and don't want to track empty slots as "air".
			if(containerItems.contains(item) || item.equals(Items.AIR.getTranslationKey())) continue;

			containerItems.add(item);
		}

		// Loops through the player's inventory slots and determines which ones contain
		// items that could be transferred to a particular container.
		// Then shift-clicks them (basically)
		for(int i = numContainerSlots; i < handler.slots.size(); i++){
			if(containerItems.contains(handler.getSlot(i).getStack().getItem().getTranslationKey())){
				//The shift-click call is made via ClientPlayerInteractionManager for proper client-server communication
				client.interactionManager.clickSlot(handler.syncId, i, 0, SlotActionType.QUICK_MOVE, client.player);
			}
		}
	}
}
