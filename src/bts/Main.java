package bts;

import bts.btlibrary.TerranMarineBTLibrary;
import jbt.execution.core.BTExecutorFactory;
import jbt.execution.core.ContextFactory;
import jbt.execution.core.ExecutionTask.Status;
import jbt.execution.core.IBTExecutor;
import jbt.execution.core.IBTLibrary;
import jbt.execution.core.IContext;
import jbt.model.core.ModelTask;

public class Main {

    public static void main(String[] args) {

        // create BT library
        IBTLibrary btLibrary = new TerranMarineBTLibrary();

        // create context for bt
        IContext context = ContextFactory.createContext(btLibrary);

        /*
         * Now we are assuming that the marine that is going to be
         * controlled has an id of "terranMarine1"
         */
        context.setVariable("CurrentEntityID","terranMarine1");

        // get TerranMarine's Model BT
        ModelTask terranMarineTree = btLibrary.getBT("TerranMarine");

        // create btExecutor for bt
        IBTExecutor btExecutor = BTExecutorFactory.createBTExecutor(terranMarineTree, context);

        // run through btExecutor
        do {
            btExecutor.tick();
        } while (btExecutor.getStatus() == Status.RUNNING);
    }

}
