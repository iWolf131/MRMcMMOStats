package me.wolf_131.mrmcmmostats;

import com.gmail.nossr50.api.ExperienceAPI;
import com.gmail.nossr50.datatypes.skills.SkillType;
import com.gmail.nossr50.mcMMO;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main extends JavaPlugin implements Listener {

    /*
     *  Variaveis do menu
     */
    private static String titulo;
    private static int gui_tamanho;
    /*
     *  Variaveis das habilidades
     */
    private static HashMap<Integer, Integer> itens_slot;
    private static HashMap<Integer, ItemStack> itens_id;
    private static HashMap<Integer, String> habilidades;
    private static List<String> lore;
    private static List<String> lore_vip;
    /*
     *  Variaveis da cabeca
     */
    private static List<String> lore_cbc;
    private static String nome_cbc;
    private static int slot_cbc;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        itens_id = new HashMap<Integer, ItemStack>();
        itens_slot = new HashMap<Integer, Integer>();
        lore = new ArrayList<String>();
        lore_vip = new ArrayList<String>();
        lore_cbc = new ArrayList<String>();
        habilidades = new HashMap<Integer, String>();
        carregarConfig();
        getCommand("stats").setExecutor(new Commands());
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {

    }

    public void carregarConfig() {
        ConfigurationSection habilidades = getConfig().getConfigurationSection("Habilidades");
        int i = 0;
        for (String st : habilidades.getKeys(false))
            if (getConfig().getBoolean("Habilidades." + st + ".Ativo")) {
                Main.habilidades.put(i, st);
                Material mat;
                int data = 0;
                if (getConfig().getString("Habilidades." + st + ".Material").contains(":")) {
                    String[] mater = getConfig().getString("Habilidades." + st + ".Material").split(":");
                    mat = Material.getMaterial(Integer.valueOf(mater[0]));
                    data = Integer.valueOf(mater[1]);
                } else
                    mat = Material.getMaterial(getConfig().getInt("Habilidades." + st + ".Material"));
                itens_id.put(i, new ItemBuilder(mat)
                        .durability(data)
                        .name(getConfig().getString("Habilidades." + st + ".Nome").replace("&", "§"))
                        .build());
                itens_slot.put(i, getConfig().getInt("Habilidades." + st + ".Slot"));
                i++;
            }
        lore = getConfig().getStringList("Lore");
        lore_vip = getConfig().getStringList("Lore-VIP");
        lore_cbc = getConfig().getStringList("Cabeca.Lore");
        nome_cbc = getConfig().getString("Cabeca.Nome");
        slot_cbc = getConfig().getInt("Cabeca.Slot");

        titulo = getConfig().getString("Gui.Titulo");
        gui_tamanho = getConfig().getInt("Gui.Tamanho");
    }

    public static void openMenu(Player p) {
        Inventory inv = Bukkit.createInventory(null, gui_tamanho, titulo.replace("&", "§"));
        int nivel = 0, slot = 0, rank = 0;
        String habilidade;
        ItemStack item;
        List<String> lores = new ArrayList<String>();
        for (int i = 0; i < habilidades.size(); i++) {
            lores.clear();
            item = itens_id.get(i);
            slot = itens_slot.get(i);
            habilidade = habilidades.get(i);
            nivel = ExperienceAPI.getLevel(p, habilidade);
            rank = mcMMO.getDatabaseManager().readRank(p.getName()).get(SkillType.getSkill(habilidade));

            if (!p.hasPermission("mrmcmmostats.vip"))
                for (String st : lore) {
                    st = st.replace("{nivel}", "" + nivel);
                    st = st.replace("{rank}", "" + rank);
                    st = st.replace("&", "§");
                    lores.add(st);
                }
            else
                for (String st : lore_vip) {
                    st = st.replace("{nivel}", "" + nivel);
                    st = st.replace("{rank}", "" + rank);
                    st = st.replace("&", "§");
                    lores.add(st);
                }
            ItemMeta meta = item.getItemMeta();
            meta.setLore(lores);
            item.setItemMeta(meta);
            inv.setItem(slot, item);
        }

        List<String> lore = new ArrayList<String>();

        nivel = ExperienceAPI.getPowerLevel(p);
        rank = mcMMO.getDatabaseManager().readRank(p.getName()).get(null);
        int combate = 0;
        for (SkillType skill : SkillType.COMBAT_SKILLS)
            combate += ExperienceAPI.getLevel(p, skill.getName());

        for (String st : lore_cbc) {
            st = st.replace("{nivel}", "" + nivel);
            st = st.replace("{rank}", "" + rank);
            st = st.replace("{combate}", "" + combate);
            st = st.replace("&", "§");
            lore.add(st);
        }

        inv.setItem(slot_cbc, new ItemBuilder(Material.SKULL_ITEM)
                    .durability(3)
                    .owner(p.getName())
                    .name(nome_cbc.replace("{nome}", p.getName()))
                    .listLore(lore)
                    .build());

        p.openInventory(inv);
    }
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getInventory().getTitle().equals(titulo.replace("&", "§"))) e.setCancelled(true);
    }

}
