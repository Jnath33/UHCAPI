package fr.jnath.UHCAPI.bukkit.plugin;

import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import fr.jnath.UHCAPI.bukkit.plugin.task.GameTask;
import fr.jnath.UHCAPI.game.Game;
import fr.jnath.UHCAPI.game.Status;
import fr.jnath.UHCAPI.hub.HubTask;
import fr.jnath.UHCAPI.manager.RegisterManager;
import fr.jnath.UHCAPI.scenario.MainScenario;
import fr.jnath.UHCAPI.scenario.Scenario;
import fr.virthia.utils.GUI.*;
import fr.virthia.utils.item.ItemCreator;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.util.*;

@SuppressWarnings("deprecation")
public class UhcAPI extends JavaPlugin {

	private static String worldHubName;
	private static World worldHub;
	private static UhcAPI instance;
	private static LocalWorld lWorldHub;
	private static double xSpawn;
	private static double ySpawn;
	private static double zSpawn;
	private static String worldShematiqueName;
	private static World worldShematique;
	private static LocalWorld lWorldShematique;
	private static String worldName;
	private static World world;
	private static LocalWorld lWorld;
	private static World netherWorld;
	private static Game game;
	private YamlConfiguration guiTextConfiguration;
	private YamlConfiguration messageTextConfiguration;
	private HubTask hubTask;

	public void onEnable() {
		System.out.println("UHCAPI Unabled");
		//init GUI
		initMenu();

		//save configs
		saveDefaultConfig();

		instance=this;

		if(!(new File("plugins/"+this.getName()+"/text").exists())){
			new File("plugins/"+this.getName()+"/text").mkdir();
		}
		saveResource("gui.yml", false);
		saveResource("message.yml", false);

		YamlConfiguration.loadConfiguration(new File("plugins/"+this.getName()+"/gui.yml"));
		YamlConfiguration.loadConfiguration(new File("plugins/"+this.getName()+"/message.yml"));

		Bukkit.getScheduler().runTaskAsynchronously(this, () ->{
			try {
				Thread.sleep(10000);
				Scenario.createScenarioInventory();
			} catch (InterruptedException e){
				e.printStackTrace();
			}
		});
		Bukkit.getScheduler().runTaskAsynchronously(this, () ->{
			try {
				Thread.sleep(10000);
				MainScenario.createScenarioInventory();
			} catch (InterruptedException e){
				e.printStackTrace();
			}
		});

		//create a instance for the game
		game = new Game(getConfig().getString("UHCApi.data.tag").replace("&","??"));

		//create and init all world (normal world, 1 for hub, 1 for miniGame)
		//and set hub spawn coordonate
		{
			worldHubName = getConfig().getString("UHCApi.hub.world");
			worldShematiqueName = getConfig().getString("UHCApi.worldMiniGame");
			worldName = getConfig().getString("UHCApi.world");

			getServer().createWorld(new WorldCreator(worldHubName));
			getServer().createWorld(new WorldCreator(worldShematiqueName));

			worldHub = Bukkit.getWorld(worldHubName);
			worldShematique = Bukkit.getWorld(worldShematiqueName);
			world = Bukkit.getWorld(worldName);
			netherWorld = Bukkit.getWorld(worldName+"_nether");

			lWorld = BukkitUtil.getLocalWorld(worldHub);
			lWorld = BukkitUtil.getLocalWorld(worldShematique);
			lWorld = BukkitUtil.getLocalWorld(world);

			xSpawn = getConfig().getDouble("UHCApi.hub.spawn.x");
			ySpawn = getConfig().getDouble("UHCApi.hub.spawn.y");
			zSpawn = getConfig().getDouble("UHCApi.hub.spawn.z");
		}

		//initialisation
		new RegisterManager().register(this);

		//start hubTask
		hubTask=new HubTask();
		hubTask.runTaskTimer(this,0,10);
	}

	private void initMenu() {
		//main_menu
		{
			Map<Integer, ImmutablePair<ItemStack, Button>> main_menu_map_button = new HashMap<Integer, ImmutablePair<ItemStack, Button>>();
			main_menu_map_button.put(0, new ImmutablePair<ItemStack, Button>(
					new ItemCreator("??cOption", Material.LEVER, 1, new String[] {
							"??aConfiguration des r??glage de base de l'uhc"
					}).make(),new Button(null, player -> {
						GUI.openGUI("uhc_config", player);
					},null,null,"button")
			));
			main_menu_map_button.put(1, new ImmutablePair<ItemStack, Button>(
					new ItemCreator("??cStarter stuff", Material.COOKED_BEEF, 1, new String[] {
							"??aStuff de d??part donner ?? tous",
							"??ales joueurs au d??but de l'uhc"
					}).make(),
					new Button(null, player -> {
						GUI.openGUI("starter_stuff", player);
					},null,null,"button")
			));
			main_menu_map_button.put(2, new ImmutablePair<ItemStack, Button>(
					new ItemCreator("??cSc??narios de la partie", Material.APPLE, 1, new String[] {
							"??aLes sc??narios qui change la fa??on de jouer",
							"??aEx : cutclean : fait cuir les minerai",
							"??ateaminventory : mets a profis un inventaire d'??quipe"
					}).make(),
					new Button(null, player -> {
						GUI.openGUI("scenario_menu", player);
					},null,null,"button")
			));
			main_menu_map_button.put(3, new ImmutablePair<ItemStack, Button>(
					new ItemCreator("??cSave config", Material.REDSTONE, 1, new String[] {
							"??aPermet de sauvgarder la configuration",
							"??acela inclut de sauvgarder les sc??narios",
							"??adans la partie, leur configuration, les",
							"??aconfiguration de l'uhc et le stuff de d??part"
					}).make(),
					new Button(null, player -> {
						GUI.openGUI("save_config_menu", player);
					},null,null,"button")
			));
			main_menu_map_button.put(4, new ImmutablePair<ItemStack, Button>(
					new ItemCreator("??cSc??nario principale de la partie", Material.GRASS, 1, new String[] {
							"??aLes sc??narios qui change le but final",
							"??aEx : Lord of ring uhc : trouver les ennemis",
							"??apour gagner avec son camps",
							"??a  FFA : UHC chaqu'un pour sa peau"
					}).make(),
					new Button(null, player -> {
						GUI.openGUI("scenario_main_menu", player);
					},null,null,"button")
			));
			main_menu_map_button.put(5, new ImmutablePair<ItemStack, Button>(
					new ItemCreator("??cLoad config", Material.PAPER, 1, new String[]
							{"??o??aRecharger vos configurations que",
									"??o??avous avez enregister au pr??alable"}).make(),
					new Button(null, player -> {
						GUI.openGUI("load_config_menu", player);
					},null,null,"button")
			));
			main_menu_map_button.put(6, new ImmutablePair<ItemStack, Button>(
					new ItemCreator("??2Start", Material.WOOL, 1, (byte) 5).make(),
					new Button(null, player -> {
						GUI.openGUI("accept_start_gui", player);
					},null,null,"button")
			));

			DefauldGUIType.SMOOTH.generateGUI(Type.MENU, "main_menu", main_menu_map_button, "??cMenu principal");
		}

		//uhc_config
		{
			Map<Integer, ImmutablePair<ItemStack, Button>> uhc_config_map_button = new HashMap<>();
			Map<Integer, Displayer> uhc_config_map_displayer = new HashMap<>();

			uhc_config_map_button.put(2, new ImmutablePair<>(
					new ItemCreator("??1??2Config timer", Material.COMPASS, 1).make(),
					new Button(null, player -> {
						GUI.openGUI("uhc_config_timer", player);
					},null,null,"button")
			));

			uhc_config_map_button.put(6, new ImmutablePair<ItemStack, Button>(
					new ItemCreator("??1Allowed potion", Material.POTION, 1, (byte) 2).make(),
					new Button(null, player -> {
						GUI.openGUI("uhc_config_allowed_potion", player);
					},null,null,"button")
			));

			uhc_config_map_button.put(7, new ImmutablePair<ItemStack, Button>(
					new ItemCreator("??1??3Border", Material.STAINED_GLASS_PANE, 1, (byte) 3).make(),
					new Button(null, player -> {
						GUI.openGUI("uhc_config_border", player);
					},null,null,"button")
			));

			uhc_config_map_button.put(0, new ImmutablePair<ItemStack, Button>(
					new ItemCreator("??1Back to main menu", Material.ARROW, 1).make(),
					new Button(null, player -> {
						GUI.openGUI("main_menu", player);
					},null,null,"button")
			));

			ImmutablePair<ItemStack, Button> put = uhc_config_map_button.put(3, new ImmutablePair<ItemStack, Button>(
					new ItemCreator("??8Drop d'item", Material.FLINT, 1).make(),
					new Button(null, player -> {
						GUI.openGUI("uhc_config_drop", player);
					}, null, null, "button")
			));
			/*
			uhc_config_map_button.put(4, new ImmutablePair<ItemStack, Button>(
					Utils.createItem("??1??2+ ??pisode pvp start", Material.STAINED_GLASS_PANE, 1, (byte) 5),
					new Button(null, player -> {
						GUI.getGUI("uhc_config").addToDisplayer(1,"pvp_start_episode");
					},null,null,"button")
			));

			uhc_config_map_button.put(8, new ImmutablePair<ItemStack, Button>(
					Utils.createItem("??1??c- ??pisode pvp start", Material.STAINED_GLASS_PANE, 1, (byte) 14),
					new Button(null, player -> {
						GUI.getGUI("uhc_config").addToDisplayer(-1,"pvp_start_episode");
					},null,null,"button")
			));

			uhc_config_map_button.put(5, new ImmutablePair<ItemStack, Button>(
					Utils.createItem("??1??2+ Invurability duration seconde", Material.STAINED_GLASS_PANE, 1, (byte) 5),
					new Button(null, player -> {
						GUI.getGUI("uhc_config").addToDisplayer(2,"invulnerability_duration");
					},null,null,"button")
			));

			uhc_config_map_button.put(9, new ImmutablePair<ItemStack, Button>(
					Utils.createItem("??1??c- Invurability duration seconde", Material.STAINED_GLASS_PANE, 1, (byte) 14),
					new Button(null, player -> {
						GUI.getGUI("uhc_config").addToDisplayer(-2,"invulnerability_duration");
					},null,null,"button")
			));


			uhc_config_map_button.put(10, new ImmutablePair<ItemStack, Button>(
					Utils.createItem("??1??8Pregenerate world", Material.COAL, 1),
					new Button(null, player -> {
						game.info("??aPregenerate world");
						game.info("??aLag jusqu'a la fin de la pr??g??n??ration");
							List<Double> doubleList = new ArrayList<>();
							for(double x=-(GUI.getGUI("uhc_config").getDisplayerValue("border_size")/2);
								x<=GUI.getGUI("uhc_config").getDisplayerValue("border_size")/2 ; x+=16){
								doubleList.add(x);
							}
							generateColone(doubleList, 0);
					},null,null,"button")
			));

			uhc_config_map_displayer.put(0, new Displayer(
					DisplayerType.VALUE,
					"border_size", "??cBorder size ", "m",
					Material.STAINED_GLASS_PANE, new String[] {},
					1,60,100,6, (byte) 1, (byte) 1
			));
			uhc_config_map_displayer.put(1, new Displayer(
					DisplayerType.VALUE,
					"episode_duration", "??cDurr?? d'un ??pisode ", "minute",
					Material.DIAMOND, new String[] {},
					5,60,1,20
			));
			uhc_config_map_displayer.put(2, new Displayer(
					DisplayerType.VALUE,
					"pvp_start_episode", "??cPvp start at ??pisode ", "",
					Material.IRON_SWORD, new String[] {},
					1,15,1,2
			));
			uhc_config_map_displayer.put(3, new Displayer(
					DisplayerType.VALUE,
					"invulnerability_duration", "??cDurr?? de l'invuln??rabilit?? : ", "s",
					Material.DIAMOND_CHESTPLATE, new String[] {},
					1,120,1,15
			));
			*/
			DefauldGUIType.CORNER.generateGUI(Type.MENU,
					"uhc_config",
					uhc_config_map_button,
					uhc_config_map_displayer,
					"??cMenu de configuration");

		}

		//uhc_config_timer
		{
			Map<Integer, ImmutablePair<ItemStack, Button>> uhc_config_map_button = new HashMap<Integer, ImmutablePair<ItemStack, Button>>();
			Map<Integer, Displayer> uhc_config_map_displayer = new HashMap<Integer, Displayer>();

			uhc_config_map_button.put(3, new ImmutablePair<ItemStack, Button>(
					new ItemCreator("??1??2+ ??pisode duration minute", Material.STAINED_GLASS_PANE, 1, (byte) 5).make(),
					new Button(null, player -> {
						GUI.getGUI("uhc_config_timer").addToDisplayer(1,"episode_duration");
					},null,null,"button")
			));

			uhc_config_map_button.put(7, new ImmutablePair<ItemStack, Button>(
					new ItemCreator("??1??c- ??pisode duration minute", Material.STAINED_GLASS_PANE, 1, (byte) 14).make(),
					new Button(null, player -> {
						GUI.getGUI("uhc_config_timer").addToDisplayer(-1,"episode_duration");
					},null,null,"button")
			));

			uhc_config_map_button.put(4, new ImmutablePair<ItemStack, Button>(
					new ItemCreator("??1??2+ Pvp start minute", Material.STAINED_GLASS_PANE, 1, (byte) 5).make(),
					new Button(null, player -> {
						GUI.getGUI("uhc_config_timer").addToDisplayer(1,"pvp_start_episode");
					},null,null,"button")
			));

			uhc_config_map_button.put(8, new ImmutablePair<ItemStack, Button>(
					new ItemCreator("??1??c- Pvp start minute", Material.STAINED_GLASS_PANE, 1, (byte) 14).make(),
					new Button(null, player -> {
						GUI.getGUI("uhc_config_timer").addToDisplayer(-1,"pvp_start_episode");
					},null,null,"button")
			));

			uhc_config_map_button.put(5, new ImmutablePair<ItemStack, Button>(
					new ItemCreator("??1??2+ Invurability duration seconde", Material.STAINED_GLASS_PANE, 1, (byte) 5).make(),
					new Button(null, player -> {
						GUI.getGUI("uhc_config_timer").addToDisplayer(2,"invulnerability_duration");
					},null,null,"button")
			));

			uhc_config_map_button.put(9, new ImmutablePair<ItemStack, Button>(
					new ItemCreator("??1??c- Invurability duration seconde", Material.STAINED_GLASS_PANE, 1, (byte) 14).make(),
					new Button(null, player -> {
						GUI.getGUI("uhc_config_timer").addToDisplayer(-2,"invulnerability_duration");
					},null,null,"button")
			));

			uhc_config_map_button.put(2, new ImmutablePair<ItemStack, Button>(
					new ItemCreator("??1Back to main menu", Material.ARROW, 1).make(),
					new Button(null, player -> {
						GUI.openGUI("main_menu", player);
					},null,null,"button")
			));

			uhc_config_map_button.put(6, new ImmutablePair<ItemStack, Button>(
					new ItemCreator("??1Back to configuration menu", Material.FLINT, 1).make(),
					new Button(null, player -> {
						GUI.openGUI("uhc_config", player);
					},null,null,"button")
			));
			uhc_config_map_displayer.put(1, new Displayer(
					"episode_duration", "??cDurr?? d'un ??pisode ", " minute",
					Material.DIAMOND, new String[] {},
					5,60,1,20
			));
			uhc_config_map_displayer.put(2, new Displayer(
					"pvp_start_episode", "??cPvp start ", " min",
					Material.IRON_SWORD, new String[] {},
					1,60,1,20
			));
			uhc_config_map_displayer.put(3, new Displayer(
					"invulnerability_duration", "??cDurr?? de l'invuln??rabilit?? : ", "s",
					Material.DIAMOND_CHESTPLATE, new String[] {},
					1,120,1,15
			));

			DefauldGUIType.CORNER.generateGUI(Type.MENU,
					"uhc_config_timer",
					uhc_config_map_button,
					uhc_config_map_displayer,
					"??cTimers config");
		}

		//uhc_config_border
		{
			Map<Integer, ImmutablePair<ItemStack, Button>> uhc_config_map_button = new HashMap<Integer, ImmutablePair<ItemStack, Button>>();
			Map<Integer, Displayer> uhc_config_map_displayer = new HashMap<Integer, Displayer>();

			uhc_config_map_button.put(2, new ImmutablePair<ItemStack, Button>(
					new ItemCreator("??1??2+ Border minute start", Material.STAINED_GLASS_PANE, 1, (byte) 5).make(),
					new Button(player -> {
						GUI.getGUI("uhc_config_border").addToDisplayer(1,"border_start");}, player -> {
						GUI.getGUI("uhc_config_border").addToDisplayer(10,"border_start");
					},null,null,"button")
			));

			uhc_config_map_button.put(6, new ImmutablePair<ItemStack, Button>(
					new ItemCreator("??1??c- Border minute start", Material.STAINED_GLASS_PANE, 1, (byte) 14).make(),
					new Button(player -> {
						GUI.getGUI("uhc_config_border").addToDisplayer(-1,"border_start");}, player -> {
						GUI.getGUI("uhc_config_border").addToDisplayer(-10,"border_start");
					},null,null,"button")
			));

			uhc_config_map_displayer.put(0, new Displayer(
					"border_start", "??cBorder start at : ", "min",
					Material.COMPASS, new String[] {},
					1,180,1,6
			));

			uhc_config_map_button.put(3, new ImmutablePair<ItemStack, Button>(
					new ItemCreator("??1??2+ Border size", Material.STAINED_GLASS_PANE, 1, (byte) 5).make(),
					new Button(null, player -> {
						GUI.getGUI("uhc_config_border").addToDisplayer(1,"border_size");
					},null,null,"button")
			));

			uhc_config_map_button.put(7, new ImmutablePair<ItemStack, Button>(
					new ItemCreator("??1??c- Border size", Material.STAINED_GLASS_PANE, 1, (byte) 14).make(),
					new Button(null, player -> {
						GUI.getGUI("uhc_config_border").addToDisplayer(-1,"border_size");
					},null,null,"button")
			));

			uhc_config_map_displayer.put(1, new Displayer(
					"border_size", "??cBorder size : ", "m",
					Material.STAINED_GLASS_PANE, new String[] {},
					1,15,100,4,(byte) 2,(byte) 2
			));

			uhc_config_map_button.put(4, new ImmutablePair<ItemStack, Button>(
					new ItemCreator("??1??2+ Block/s", Material.STAINED_GLASS_PANE, 1, (byte) 5).make(),
					new Button(null, player -> {
						GUI.getGUI("uhc_config_border").addToDisplayer(1,"border_speed");
					},null,null,"button")
			));

			uhc_config_map_button.put(8, new ImmutablePair<ItemStack, Button>(
					new ItemCreator("??1??c- Block/s", Material.STAINED_GLASS_PANE, 1, (byte) 14).make(),
					new Button(null, player -> {
						GUI.getGUI("uhc_config_border").addToDisplayer(-1,"border_speed");
					},null,null,"button")
			));

			uhc_config_map_displayer.put(2, new Displayer(
					"border_speed", "??cBorder speed (Block/s) : ", " B/s",
					Material.COMPASS, new String[] {},
					1,8,0.5,1
			));

			uhc_config_map_button.put(5, new ImmutablePair<ItemStack, Button>(
					new ItemCreator("??1??2+ Border size final", Material.STAINED_GLASS_PANE, 1, (byte) 5).make(),
					new Button(null, player -> {
						GUI.getGUI("uhc_config_border").addToDisplayer(1,"border_size_end");
					},null,null,"button")
			));

			uhc_config_map_button.put(9, new ImmutablePair<ItemStack, Button>(
					new ItemCreator("??1??c- Border size final", Material.STAINED_GLASS_PANE, 1, (byte) 14).make(),
					new Button(null, player -> {
						GUI.getGUI("uhc_config_border").addToDisplayer(-1,"border_size_end");
					},null,null,"button")
			));

			uhc_config_map_displayer.put(3, new Displayer(
					"border_size_end", "??cBorder size final : ", "m",
					Material.STAINED_GLASS_PANE, new String[] {},
					2,40,10,5,(byte) 2,(byte) 2
			));

			uhc_config_map_button.put(10, new ImmutablePair<ItemStack, Button>(
					new ItemCreator("??1??8Pregenerate world", Material.COAL, 1, new String[] {
							"??aRisque de ??5??llag??r??a important"
					}).make(),
					new Button(null, player -> {
						game.info("??1??aPregenerate world");
						game.info("??aLag jusqu'a la fin de la pr??g??n??ration");
						List<Double> doubleList = new ArrayList<>();
						for(double x=-(GUI.getGUI("uhc_config_border").getDisplayerValue("border_size")/2);
							x<=GUI.getGUI("uhc_config_border").getDisplayerValue("border_size")/2 ; x+=16){
							doubleList.add(x);
						}
						generateColone(doubleList, 0);
					},null,null,"button")
			));

			DefauldGUIType.CORNER.generateGUI(Type.MENU,
					"uhc_config_border",
					uhc_config_map_button,
					uhc_config_map_displayer,
					"??cConfig de la bordure");
		}

		//uhc_config_drop
		{
			Map<Integer, ImmutablePair<ItemStack, Button>> uhc_config_drop_map_button = new HashMap<>();
			Map<Integer, Displayer> uhc_config_drop_map_displayer = new HashMap<>();

			uhc_config_drop_map_button.put(2, new ImmutablePair<ItemStack, Button>(
					new ItemCreator("??1??2+", Material.STAINED_GLASS_PANE, 1, (byte) 5).make(),
					new Button(null, player -> {
						GUI.getGUI("uhc_config_drop").addToDisplayer(2,"apple");
					},null,null,"button")
			));
			uhc_config_drop_map_button.put(6, new ImmutablePair<ItemStack, Button>(
					new ItemCreator("??1??c-", Material.STAINED_GLASS_PANE, 1, (byte) 14).make(),
					new Button(null, player -> {
						GUI.getGUI("uhc_config_drop").addToDisplayer(-2, "apple");
					},null,null,"button")
			));
			uhc_config_drop_map_button.put(3, new ImmutablePair<ItemStack, Button>(
					new ItemCreator("??1??2+", Material.STAINED_GLASS_PANE, 1, (short) 5).make(),
					new Button(null, player -> {
						GUI.getGUI("uhc_config_drop").addToDisplayer(2,"golden_apple");
					},null,null,"button")
			));
			uhc_config_drop_map_button.put(7, new ImmutablePair<ItemStack, Button>(
					new ItemCreator("??1??c-", Material.STAINED_GLASS_PANE, 1, (short) 14).make(),
					new Button(null, player -> {
						GUI.getGUI("uhc_config_drop").addToDisplayer(-2, "golden_apple");
					},null,null,"button")
			));
			uhc_config_drop_map_button.put(4, new ImmutablePair<ItemStack, Button>(
					new ItemCreator("??1??2+", Material.STAINED_GLASS_PANE, 1, (short) 5).make(),
					new Button(null, player -> {
						GUI.getGUI("uhc_config_drop").addToDisplayer(2,"flint");
					},null,null,"button")
			));
			uhc_config_drop_map_button.put(8, new ImmutablePair<ItemStack, Button>(
					new ItemCreator("??1??c-", Material.STAINED_GLASS_PANE, 1, (short) 14).make(),
					new Button(null, player -> {
						GUI.getGUI("uhc_config_drop").addToDisplayer(-2, "flint");
					},null,null,"button")
			));
			uhc_config_drop_map_button.put(5, new ImmutablePair<ItemStack, Button>(
					new ItemCreator("??1Back to UHC config page", Material.ARROW, 1).make(),
					new Button(null, player -> {
						GUI.openGUI("uhc_config", player);
					},null,null,"button")
			));
			uhc_config_drop_map_button.put(9, new ImmutablePair<ItemStack, Button>(
					new ItemCreator("??1Back to main menu", Material.ARROW, 1).make(),
					new Button(null, player -> {
						GUI.openGUI("main_menu", player);
					},null,null,"button")
			));

			uhc_config_drop_map_displayer.put(0, new Displayer(
					"apple","??a??l","%",Material.APPLE,new String[] {}, 2, 60,1,20
			));
			uhc_config_drop_map_displayer.put(1, new Displayer(
					"golden_apple","??a??l","%",Material.GOLDEN_APPLE,new String[] {}, 0, 60,0.1,0
			));
			uhc_config_drop_map_displayer.put(2, new Displayer(
					"flint","??a??l","%",Material.FLINT,new String[] {}, 2, 60,1,20
			));
			DefauldGUIType.CORNER.generateGUI(Type.MENU, "uhc_config_drop", uhc_config_drop_map_button, uhc_config_drop_map_displayer, "??8Drop item config");
		}

		//uhc_config_allowed_potion
		{
			DefauldGUIType.CORNER.generateGUI(Type.MENU, "uhc_config_allowed_potion", new HashMap<>(), "??eS??lection des potions");

			GUI gui = GUI.getGUI("uhc_config_allowed_potion");

			//gui.addDisplayer();
		}

		//starter_stuff
		{
			Map<Integer, ImmutablePair<ItemStack, Button>> starter_stuff_map_button = new HashMap<Integer, ImmutablePair<ItemStack, Button>>();

			starter_stuff_map_button.put(0, new ImmutablePair<ItemStack, Button>(
					new ItemCreator("??1Back to main menu", Material.ARROW, 1).make(),
					new Button(null,	player -> {
						GUI.openGUI("main_menu", player);
					},null,null,"button")
			));

			starter_stuff_map_button.put(1, new ImmutablePair<ItemStack, Button>(
					new ItemCreator("??1Switch Gamemode", Material.STONE_BUTTON, 1, new String[]{
							"??aPour passer du mode survie a cr??atif pour",
							"??apouvoir remplir le starter stuff"
					}).make(),
					new Button(null,	player -> {
						if (player.getGameMode() == GameMode.ADVENTURE) {
							player.setGameMode(GameMode.CREATIVE);
						} else {
							player.setGameMode(GameMode.ADVENTURE);
						}
					},null,null,"button")
			));

			DefauldGUIType.VOID.generateGUI(Type.SETSTUFF, "starter_stuff", starter_stuff_map_button, "??cStuff de d??part de l'uhc");
		}

		//accept gui
		{
			//start
			DefauldGUIType.ACCEPT.generateAcceptGUI(GUI.getGUI("main_menu"), this::start, "accept_start_gui", "??aAccept??7/??cDeny??5 start");

			//world pregeneration
		}
	}

	public void start(Player p) {
		if(!MainScenario.getMainScenario().canStart(p)) return;

		//end hubTask
		hubTask.cancel();

		//set game status
		game.setStatus(Status.INVULERABILITY);
		p.sendMessage(String.valueOf(GUI.getGUI("uhc_config_border").getDisplayerValue("border_size")));

		//set world border size
		world.getWorldBorder().setSize(GUI.getGUI("uhc_config_border").getDisplayerValue("border_size"));

		//create random generator with init with the current time in millis seconde
		Random rand = new Random(System.currentTimeMillis());
		p.sendMessage("test 4");

		//create player iterator for teleport all player

		//envoie d'un message pour informer les joueurs que l'uhc se lance
		getGame().info("L'uhc se lance");
		//set variable for T??l??port player message
		final int nomberOfPlayer = Bukkit.getOnlinePlayers().size();
		int playerNomber=1;

		//run for iterator and t??l??port players
		for(Player pls : Bukkit.getOnlinePlayers()) {
			ItemStack itemStack = new ItemStack(Material.ENCHANTED_BOOK, 1);
			EnchantmentStorageMeta meta = (EnchantmentStorageMeta) itemStack.getItemMeta();
			meta.addStoredEnchant(Enchantment.ARROW_INFINITE, 1, true);
			itemStack.setItemMeta(meta);

			//send massage
			getGame().info("T??l??port player [" + playerNomber + "/" + nomberOfPlayer + "]");

			//incrice the player nomber for display
			playerNomber++;

			//get the next player


			//create relative loc for teleport the player
			double x = rand.nextDouble() * world.getWorldBorder().getSize() - world.getWorldBorder().getSize() / 2.0D;
			double z = rand.nextDouble() * world.getWorldBorder().getSize() - world.getWorldBorder().getSize() / 2.0D;

			//player teleport in location
			pls.teleport(new Location(world, x + world.getWorldBorder().getCenter().getX(), 150.0D, z + world.getWorldBorder().getCenter().getZ()));

			//reset inventory
			pls.getInventory().clear();

			//reset hearth and add effect
			pls.setMaxHealth(20);
			pls.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20*60*10,10));
			pls.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*60*10,10));

			//add player to the game
			game.getPlayers().add(pls);

			//set inventory
			for (ItemStack item : GUI.getGUI("starter_stuff").getInventory()) {
				if (item != null && item.getType() != null) {
					if (item.getItemMeta() == null || item.getItemMeta().getDisplayName() == null || (!item.getItemMeta().getDisplayName().startsWith("??1"))) {
						pls.getInventory().addItem(item);
						pls.updateInventory();
					}
				}
			}
		}

		for(Player player : Bukkit.getOnlinePlayers()){
			for(PotionEffect potionEffect : player.getActivePotionEffects()){
				player.removePotionEffect(potionEffect.getType());
			}
		}

		//run all scenario
		for(Scenario scenario : Scenario.createActivatedScenarioList()){
			scenario.run();
		}
		MainScenario.getMainScenario().run();

		new GameTask().runTaskTimer(this,0,5);
	}

	//all variable getter
	public static String getWorldHubName() {
		return worldHubName;
	}

	public static World getWorldHub() {
		return worldHub;
	}

	public static LocalWorld getlWorldHub() {
		return lWorldHub;
	}

	public static double getXSpawn() {
		return xSpawn;
	}

	public static double getYSpawn() {
		return ySpawn;
	}

	public static double getZSpawn() {
		return zSpawn;
	}

	public static String getWorldShematiqueName() {
		return worldShematiqueName;
	}

	public static World getWorldShematique() {
		return worldShematique;
	}

	public static LocalWorld getlWorldShematique() {
		return lWorldShematique;
	}

	public static String getWorldName() {
		return worldName;
	}

	public static World getWorld() {
		return world;
	}

	public static LocalWorld getlWorld() {
		return lWorld;
	}

	public static Game getGame() {
		return game;
	}

	public static UhcAPI getInstance() {
		return instance;
	}

	private void generateColone(List<Double> doubles, int iterator){
		final Integer[] i = {iterator};
		Bukkit.getScheduler().runTaskLater(this, new Runnable() {
			@Override
			public void run() {
				double x = doubles.get(0);
				if(i[0] ==0)
					game.info("Pregenaration "+(int) ((0.5+x/GUI.getGUI("uhc_config_border").getDisplayerValue("border_size"))*100)+"%");
				i[0]++;
				if(i[0] >=8)
					i[0] =0;
				for(double y=-(GUI.getGUI("uhc_config_border").getDisplayerValue("border_size")/2);
					y<=GUI.getGUI("uhc_config_border").getDisplayerValue("border_size")/2 ; y+=16){
					new Location(world, x, 60, y).getChunk().load();
				}
				doubles.remove(0);
				if(doubles.size()>0){
					generateColone(doubles, i[0]);
				}else {
					game.info("Pregeneration end");
				}
			}
		},1);
	}

	public static World getNetherWorld() {
		return netherWorld;
	}
}
