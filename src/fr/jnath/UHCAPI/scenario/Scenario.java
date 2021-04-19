package fr.jnath.UHCAPI.scenario;

import fr.virthia.utils.GUI.*;
import fr.virthia.utils.item.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class Scenario {
    private final String name, displayName, tag, configMenuName;
    private static final List<Scenario> scenarios = new ArrayList<>();
    private static List<Scenario> scenariosActivated = new ArrayList<>();
    private final Consumer<Scenario> run;
    private final Material itemType;
    private int slot;
    private static Inventory scenarioInventory;
    private static final Map<Integer, Scenario> slotToScenario = new HashMap<>();
    private GUI gui = null;
    private final String[] lores;

    public Scenario(String name, String displayName, String tag, Material itemType, Consumer<Scenario> run, String[] lores) {
        this.name = name;
        this.displayName = displayName;
        this.tag = tag;
        this.run=run;
        this.configMenuName = null;
        this.itemType=itemType;
        scenarios.add(this);
        this.lores=lores;
    }

    public Scenario(String name, String displayName, String tag, String configMenuName, Material itemType, Consumer<Scenario> run, String[] lores) {
        this.name = name;
        this.displayName = displayName;
        this.tag = tag;
        this.run=run;
        this.configMenuName = configMenuName;
        this.itemType=itemType;
        scenarios.add(this);
        this.gui=GUI.getGUI(configMenuName);
        this.lores=lores;
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

    public static void setScenariosActivated(List<Scenario> scenariosActivated) {
        Scenario.scenariosActivated = scenariosActivated;
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
        return GUI.getGUI("scenario_menu").getDisplayerBoolean(name+"_displayer_in_scenario_menu");
    }

    public static List<Scenario> getScenariosActivated() {
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
        scenarioInventory = DefauldGUIType.VOID.generateGUI(Type.MENU, "scenario_menu", new HashMap<>(), "§cSélection des scénario");
        GUI gui = GUI.getGUI("scenario_menu");
        int iterator = 9;
        gui.addButton(new Button(null, player -> {
            GUI.openGUI("main_menu", player);
        },null,null,"back_to_main_menu_button"), 4);
        scenarioInventory.setItem(4, new ItemCreator("§1Back to main menu",Material.ARROW,1).make());
        for(Scenario scenario : scenarios){
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
                gui.switchDisplayer(scenario.name+"_displayer_in_scenario_menu");
            },null,null,"scenario"), iterator);
            iterator++;
        }
    }

    public static List<Scenario> createActivatedScenarioList(){
        List<Scenario> tempList = new ArrayList<>();
        for(Scenario scenario : scenarios){
            if(scenario.isActivated()){
                tempList.add(scenario);
            }
        }
        return tempList;
    }
}
