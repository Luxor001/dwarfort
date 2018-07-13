package utilities;

import java.util.Random;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.NumberTermImpl;
import jason.asSyntax.Term;

public class random extends DefaultInternalAction {

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {     
        NumberTerm lbt = (NumberTerm)args[0];
        NumberTerm ubt = (NumberTerm)args[1];
        int lb = (int) lbt.solve();
        int ub = (int) ubt.solve();
        
        Random randgen = new Random();
        int num = lb;
        num += randgen.nextInt(ub-lb);
        NumberTerm res = new NumberTermImpl(num);
		return un.unifies(args[2], res); 
    }
}