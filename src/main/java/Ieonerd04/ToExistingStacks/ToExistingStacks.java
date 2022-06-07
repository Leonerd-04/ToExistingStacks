package Ieonerd04.ToExistingStacks;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;


public class ToExistingStacks implements ClientModInitializer {

	public static KeyBinding MOVE_TO_CONTAINER;

	public final static String MOD_ID = "existing_stacks";
	public final static Logger LOGGER = LogManager.getLogger();

	@Override
	public void onInitializeClient(){
		MOVE_TO_CONTAINER = KeyBindingHelper.registerKeyBinding(
				new KeyBinding(
						"existing_stacks.move",
						InputUtil.Type.KEYSYM,
						GLFW.GLFW_KEY_UP,
						"existing_stacks.category"
				)
		);
	}


}
