package kr.blugon.explosionprotection

import kr.blugon.explosionprotection.command.EPCommand
import kr.blugon.minicolor.MiniColor
import kr.blugon.minicolor.MiniColor.Companion.miniMessage
import org.bukkit.Bukkit
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockExplodeEvent
import org.bukkit.event.entity.EntityDamageByBlockEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent.DamageCause
import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class ExplosionProtection : JavaPlugin(),Listener {

    companion object {
        val configFile = File("plugins/ExplosionProtection/config.yml")
        val yaml = YamlConfiguration.loadConfiguration(configFile)

        var blockProtection = true
        var damageProtection = true

        val prefix = "${MiniColor.GRAY}[${MiniColor.BLUE}ExplosionProtection${MiniColor.BLUE.close}]${MiniColor.GRAY.close} ".miniMessage
    }

    override fun onEnable() {
        logger.info("Plugin Enable")
        Bukkit.getPluginManager().registerEvents(this, this)

        if(!configFile.exists()) saveConfig()
        yaml.load(configFile)

        getCommand("explosionprotection")!!.apply {
            setExecutor(EPCommand())
            tabCompleter = EPCommand()
        }

        blockProtection = yaml.getBoolean("blockProtection")
        damageProtection = yaml.getBoolean("damageProtection")
    }

    override fun onDisable() {
        yaml.set("blockExplosion", blockProtection)
        yaml.set("explosionDamage", damageProtection)
        yaml.save(configFile)
        logger.info("Plugin Disable")
    }


    @EventHandler
    fun blockExplode(event : BlockExplodeEvent) {
        if(!blockProtection) return
        val block = event.block
        val world = block.world
        val location = block.location.add(.5, .0, .5)
        event.isCancelled = true
        world.createExplosion(location, event.yield, false, false)
    }

    @EventHandler
    fun entityExplode(event : EntityExplodeEvent) {
        if(!blockProtection) return
        val entity = event.entity
        val world = entity.world
        val location = entity.location
        event.isCancelled = true
        world.createExplosion(location, event.yield, false, false, entity)
    }

    @EventHandler
    fun entityExplosionDamage(event : EntityDamageByEntityEvent) {
        if(!damageProtection) return
        if(event.cause != DamageCause.ENTITY_EXPLOSION) return
        event.isCancelled = true
    }
    @EventHandler
    fun blockExplosionDamage(event : EntityDamageByBlockEvent) {
        if(!damageProtection) return
        if(event.cause != DamageCause.BLOCK_EXPLOSION) return
        event.isCancelled = true
    }
}