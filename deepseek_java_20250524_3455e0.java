package com.example.ahsniper;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

@Mod(modid = "ahsniper", version = "1.0", clientSideOnly = true)
public class AHSniperMod {
    private boolean isEnabled = false;
    private double minProfitMargin = 0.3; // 30% under market value
    private final Map<String, Double> itemPrices = new HashMap<>();

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        ClientCommandHandler.instance.registerCommand(new CommandToggle());
        ClientCommandHandler.instance.registerCommand(new CommandConfig(this));
        
        // Load default prices
        itemPrices.put("Hyperion", 500_000_000.0);
        itemPrices.put("Necron's Blade", 300_000_000.0);
    }

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        if (!isEnabled) return;
        
        String message = event.message.getUnformattedText();
        
        // Hypixel AH format: "[AUCTION] ItemName: 1,000,000 coins"
        if (message.startsWith("[AUCTION]")) {
            String[] parts = message.split(":");
            if (parts.length < 2) return;
            
            String itemName = parts[0].replace("[AUCTION]", "").trim();
            String priceStr = parts[1].replaceAll("[^0-9]", "").trim();
            
            try {
                double price = Double.parseDouble(priceStr);
                double marketPrice = PriceFetcher.getPriceFromCoflnet(itemName);
                
                if (marketPrice > 0 && price < marketPrice * (1 - minProfitMargin)) {
                    Minecraft.getMinecraft().thePlayer.addChatMessage(
                        new ChatComponentText("§a[FLIP] §7Found §b" + itemName + " §7for §6" + price + " coins! (Avg: " + marketPrice + ")")
                    );
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    public double getMinProfitMargin() {
        return minProfitMargin;
    }

    public void setMinProfitMargin(double minProfitMargin) {
        this.minProfitMargin = minProfitMargin;
    }

    public class CommandToggle extends CommandBase {
        @Override
        public String getCommandName() {
            return "adamon";
        }

        @Override
        public String getCommandUsage(ICommandSender sender) {
            return "/adamon - Toggle AH sniper alerts";
        }

        @Override
        public void processCommand(ICommandSender sender, String[] args) throws CommandException {
            isEnabled = !isEnabled;
            Minecraft.getMinecraft().thePlayer.addChatMessage(
                new ChatComponentText("§aAH Sniper " + (isEnabled ? "§aON" : "§cOFF"))
            );
        }
    }
}