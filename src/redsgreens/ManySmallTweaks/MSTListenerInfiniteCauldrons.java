package redsgreens.ManySmallTweaks;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class MSTListenerInfiniteCauldrons implements Listener {

	private MSTPlugin Plugin;

	public MSTListenerInfiniteCauldrons(MSTPlugin plugin)
	{
		Plugin = plugin;
	}

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event)
    {
    	// return if the event is cancelled
    	if(event.isCancelled()) return;

		// return if the event is not a right-click-block action
		Action action = event.getAction();
		if(action != Action.RIGHT_CLICK_BLOCK) return;

		final Block block = event.getClickedBlock();
		
		if(block.getType() != Material.CAULDRON) return;

		if(Plugin.Config.isTweakEnabled(block.getWorld().getName(), MSTName.InfiniteCauldrons))
		{
			Player player = event.getPlayer();
			
			// return if they can't build here
			if(!Plugin.canBuild(player, block)) return;

	    	// water level is stored in the data byte
	    	if(block.getData() > 0)
	    	{
	    		// refill the cauldron
				Plugin.getServer().getScheduler().scheduleSyncDelayedTask(Plugin, new Runnable() {
				    public void run() {
				    	block.setData((byte) 3);
				    }
				}, 0);

				ItemStack itemInHand = player.getItemInHand();
				Material itemInHandMaterial = itemInHand.getType();

				if(itemInHandMaterial == Material.GLASS_BOTTLE)
				{
					itemInHand.setType(Material.POTION);
					player.setItemInHand(itemInHand);
					event.setCancelled(true);
				}
				else if(itemInHandMaterial == Material.BUCKET)
				{
					itemInHand.setType(Material.WATER_BUCKET);
					player.setItemInHand(itemInHand);
					event.setCancelled(true);
				}
	    	}

		}
		
    }
}