package eu.cloudtm;

import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * //TODO: document this!
 *
 * @author Pedro Ruivo
 * @since 5.3
 */
public class Test {

    public static void main(String[] args) {
        LinkedHashMap<String,LinkedHashMap<String,Integer>> result = new LinkedHashMap<String,LinkedHashMap<String,Integer>>();
        //String readData = (String)processedSample.getParam(Param.getByName("DAP.DapReadAccessData_0"));
        //String writeData = (String)processedSample.getParam(Param.getByName("DAP.DapWriteAccessData_0"));
        LinkedList<String> paramNameList = new LinkedList<String>();
        LinkedHashMap<String,Integer> contextStats;
        String toParse;
        String[] contexts;
        String currentContext;
        String contextName;
        String[] splitContext;
        String[] tokens;
        String domainAttribute;
        String domainClass;
        String frequency;
        toParse = "SetAgeAction_SetAgeAction:eu.cloudtm.Author.id=0;eu.cloudtm.Author.age=600;eu.cloudtm.Author.parent=0;pt.ist.fenixframework.DomainRoot.application=0;pt.ist.fenixframework.DomainRoot.theAuthors=2;pt.ist.fenixframework.DomainRoot.thePublishers=0;pt.ist.fenixframework.DomainRoot.theBooks=1;#SetPriceAction_SetPriceAction:pt.ist.fenixframework.DomainRoot.application=0;pt.ist.fenixframework.DomainRoot.theAuthors=2;pt.ist.fenixframework.DomainRoot.thePublishers=0;pt.ist.fenixframework.DomainRoot.theBooks=1;eu.cloudtm.Book.id=0;eu.cloudtm.Book.price=0;eu.cloudtm.Book.parent=0;eu.cloudtm.Book.comments=0;#default_default:#GetAuthorsInfoAction_GetAuthorsInfoAction:eu.cloudtm.Author.id=0;eu.cloudtm.Author.age=600;eu.cloudtm.Author.parent=0;pt.ist.fenixframework.DomainRoot.application=0;pt.ist.fenixframework.DomainRoot.theAuthors=2;pt.ist.fenixframework.DomainRoot.thePublishers=0;pt.ist.fenixframework.DomainRoot.theBooks=1;#";
        contexts = toParse.split("#");

        for (int i = 0; i < contexts.length; i++) {
            System.out.println("Processing " + contexts[i]);
            splitContext = contexts[i].split(":");
            if (splitContext.length == 1) {
                continue;
            }
            contextName = splitContext[0].split("_")[0];
            tokens = splitContext[1].split(";");

            if (result.containsKey(contextName)) {
                contextStats = result.get(contextName);
            } else {
                contextStats = new LinkedHashMap<String, Integer>();// domainClass - accessFrequency
                result.put(contextName, contextStats);
            }

            for (int j = 0; j < tokens.length; j++) {
                //fullyQualifiedDomainClassName.attributeName=accessFrequency
                domainAttribute = tokens[j].split("=")[0];
                frequency = tokens[j].split("=")[1];
                domainClass = domainAttribute.substring(0, domainAttribute.lastIndexOf("."));

                if (contextStats.containsKey(domainClass))
                    contextStats.put(domainClass, contextStats.get(domainClass) + (new Integer(frequency)));
                else
                    contextStats.put(domainClass, new Integer(frequency));
            }
        }
        System.out.println(result);
    }

}
