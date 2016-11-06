package org.devathon.contest2016.blocks;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.devathon.contest2016.BlockManager;

/**
 *
 * @author Autom
 */
public class CoffeeMachine extends CustomBlock {

    public CoffeeMachine(BlockManager manager, Block block) {
        super(manager, block);
    }

    @Override
    public void destroy() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Location getActivatorLocation() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean activate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
