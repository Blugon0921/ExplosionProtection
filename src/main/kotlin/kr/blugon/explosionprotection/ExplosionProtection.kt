package kr.blugon.explosionprotection

import kr.blugon.explosionprotection.command.EPCommand
import org.bukkit.Bukkit
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockExplodeEvent
import org.bukkit.event.entity.EntityDamageByBlockEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class ExplosionProtection : JavaPlugin(),Listener {

    companion object {
        val configFile = File("plugins/ExplosionProtection/config.yml")
        val yaml = YamlConfiguration.loadConfiguration(configFile)

        var blockExplosion = false
        var explosionDamage = true
    }

    override fun onEnable() {
        logger.info("Plugin Enable")
        Bukkit.getPluginManager().registerEvents(this, this)

        if(!configFile.exists()) {
            yaml.set("blockExplosion", false)
            yaml.set("explosionDamage", false)
            yaml.save(configFile)
        }
        yaml.load(configFile)

        getCommand("ep")!!.apply {
            setExecutor(EPCommand())
            tabCompleter = EPCommand()
        }

        blockExplosion = yaml.getBoolean("blockExplosion")
        explosionDamage = yaml.getBoolean("explosionDamage")
    }

    override fun onDisable() {
        yaml.set("blockExplosion", blockExplosion)
        yaml.set("explosionDamage", explosionDamage)
        yaml.save(configFile)
        logger.info("Plugin Disable")
    }


    @EventHandler
    fun blockExplode(event : BlockExplodeEvent) {
        val block = event.block
        val world = block.world
        val location = block.location.add(.5, .0, .5)
        if(!blockExplosion) {
            event.isCancelled = true
            world.createExplosion(location, event.yield, false, false)
        }
    }

    @EventHandler
    fun entityExplode(event : EntityExplodeEvent) {
        val entity = event.entity
        val world = entity.world
        val location = entity.location.add(.5, .5, .5)
        if(!blockExplosion) {
            event.isCancelled = true
            world.createExplosion(location, event.yield, false, false)
        }
    }

    @EventHandler
    fun explosionDamage(event : EntityDamageByEntityEvent) {
        if(explosionDamage) return
        if(event.cause != EntityDamageEvent.DamageCause.BLOCK_EXPLOSION &&
            event.cause != EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) return
        event.isCancelled = true
    }
    @EventHandler
    fun explosionDamage(event : EntityDamageByBlockEvent) {
        if(explosionDamage) return
        if(event.cause != EntityDamageEvent.DamageCause.BLOCK_EXPLOSION &&
            event.cause != EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) return
        event.isCancelled = true
    }
}