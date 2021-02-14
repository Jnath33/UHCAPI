package fr.jnath.UHCAPI.scenario;

import fr.jnath.UHCAPI.GUI.*;
import fr.jnath.UHCAPI.bukkit.plugin.UhcAPI;
import fr.jnath.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class MainScenario {
    private final String name, displayName, tag, configMenuName;
    private static final List<MainScenario> scenarios = new ArrayList<>();
    private static List<MainScenario> scenariosActivated = new ArrayList<>();
    private final Consumer<MainScenario> run;
    private final Material itemType;
    private int slot;
    private static Inventory scenarioInventory;
    private static final Map<Integer, MainScenario> slotToScenario = new HashMap<>();
    private GUI gui = null;
    private final String[] lores;
    private boolean useTeam;
    private int teamNomber;
    private Function<UhcAPI, Boolean> function;

    public MainScenario(String name, String displayName, String tag, Material itemType, Consumer<MainScenario> run, String[] lores, Function<UhcAPI, Boolean> function) {
        this.name = name;
        this.displayName = displayName;
        this.tag = tag;
        this.run=run;
        this.configMenuName = null;
        this.itemType=itemType;
        scenarios.add(this);
        this.lores=lores;
        useTeam=false;
        this.function=function;
    }

    public MainScenario(String name, String displayName, String tag, String configMenuName, Material itemType, Consumer<MainScenario> run, String[] lores, Function<UhcAPI, Boolean> function) {
        this.name = name;
        this.displayName = displayName;
        this.tag = tag;
        this.run=run;
        this.configMenuName = configMenuName;
        this.itemType=itemType;
        scenarios.add(this);
        this.gui=GUI.getGUI(configMenuName);
        this.lores=lores;
        useTeam=false;
        this.function=function;
    }

    public MainScenario(String name, String displayName, String tag, Material itemType, Consumer<MainScenario> run, String[] lores, int teamNomber, Function<UhcAPI, Boolean> function) {
        this.name = name;
        this.displayName = displayName;
        this.tag = tag;
        this.run=run;
        this.configMenuName = null;
        this.itemType=itemType;
        scenarios.add(this);
        this.lores=lores;
        useTeam=true;
        this.teamNomber=teamNomber;
        this.function=function;
    }

    public MainScenario(String name, String displayName, String tag, String configMenuName, Material itemType, Consumer<MainScenario> run, String[] lores, int teamNomber, Function<UhcAPI, Boolean> function) {
        this.name = name;
        this.displayName = displayName;
        this.tag = tag;
        this.run=run;
        this.configMenuName = configMenuName;
        this.itemType=itemType;
        scenarios.add(this);
        this.gui=GUI.getGUI(configMenuName);
        this.lores=lores;
        useTeam=true;
        this.teamNomber=teamNomber;
        this.function=function;
    }

    public boolean canStart(Player player){
        if(function.apply(UhcAPI.getInstance())){
            return true;
        }else {
            player.sendMessage(Tchat.get(UhcAPI.getGame().getTchatNameInfo()).getTag() +" La partie a une erreur de configuration et ne peut donc pas se lancer");
            player.closeInventory();
            return function.apply(UhcAPI.getInstance());
        }
    }

    public void run(){
        run.accept(this);
    }

    public void info(String message){
        Bukkit.broadcastMessage(tag+" "+message);
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static void setScenariosActivated(List<MainScenario> scenariosActivated) {
        MainScenario.scenariosActivated = scenariosActivated;
    }

    public static MainScenario getMainScenario(){
        if(scenariosActivated.size()>0){
            return scenariosActivated.get(0);
        }
        return null;
    }

    public String getTag() {
        return tag;
    }

    public String getConfigMenuName() {
        return configMenuName;
    }

    public GUI getGui() {
        return gui;
    }

    public boolean isActivated() {
        return gui.getDisplayerBoolean(getName()+"_displayer_in_scenario_menu");
    }

    public void toogleActivated(){
        GUI gui=GUI.getGUI("scenario_main_menu");
        boolean activated = gui.getDisplayerBoolean(getName()+"_displayer_in_scenario_menu");
        if(getScenariosActivated().size()>=1 && !activated) return;
        gui.switchDisplayer(getName()+"_displayer_in_scenario_menu");
        if(activated){
            getScenariosActivated().remove(this);
        }else {
            getScenariosActivated().add(this);
        }
    }

    public static List<MainScenario> getScenariosActivated() {
        return scenariosActivated;
    }

    public Material getItemType() {
        return itemType;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public static void createScenarioInventory() {
        scenarioInventory = DefauldGUIType.VOID.generateGUI(Type.MENU, "scenario_main_menu", new HashMap<>(), "§cChoix scenario principal");
        GUI gui = GUI.getGUI("scenario_main_menu");
        int iterator = 9;
        gui.addButton(new Button(null, player -> {
            GUI.openGUI("main_menu", player);
        },null,null,"back_to_main_menu_button"), 4);
        scenarioInventory.setItem(4, Utils.createItem("§1Back to main menu",Material.ARROW,1));
        for(MainScenario scenario : scenarios){
            scenario.setSlot(iterator);
            Displayer displayer = new Displayer(scenario.name+"_displayer_in_scenario_menu", "§1"+scenario.getDisplayName()+" : ",".",scenario.itemType, scenario.lores,false);
            gui.addDisplayer(scenario.getSlot(), displayer);
            slotToScenario.put(scenario.getSlot(), scenario);

            gui.addButton(new Button(player -> {
                if(scenario.gui!=null){
                    if(gui.getDisplayerBoolean(scenario.getName()+"_displayer_in_scenario_menu")) {
                        player.openInventory(scenario.gui.getInventory());
                    }
                }
            }
                    ,player -> {
                scenario.toogleActivated();
            },null,null,"scenario"), iterator);
            iterator++;
        }
    }

    public static List<MainScenario> createActivatedScenarioList(){
        List<MainScenario> tempList = new ArrayList<>();
        for(MainScenario scenario : scenarios){
            if(scenario.isActivated()){
                tempList.add(scenario);
            }
        }
        return tempList;
    }
}
