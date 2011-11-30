/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gendruwo;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
import jess.JessException;
import jess.Rete;
import jess.awt.TextReader;
import jess.swing.JTextAreaWriter;

/**
 *
 * @author Hendra
 */
public class JessManager {
    Rete engine = new Rete();
    JTextAreaWriter input;
    JTextArea output = new JTextArea();
    boolean done = false;

    public JessManager () {
        input = new JTextAreaWriter(output);
        engine.addOutputRouter("t", input);
        engine.addOutputRouter("WSTDOUT", input);
        engine.addOutputRouter("WSTDERR", input);
    }

    public String getOutput(){
        return output.getText();
    }

     public void executeRules(String Facts) {
        try {
            System.out.println("jreeeeeeng");
            engine.batch("mushroom.clp");
            engine.reset();
            String Assert = "(assert "+Facts+")";
            engine.eval(Assert);
            engine.run();
        } catch (JessException je) {
            je.printStackTrace();
        }
    }

    public void run() {
        try {
            engine.run();
        } catch (JessException ex) {
            Logger.getLogger(JessManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
