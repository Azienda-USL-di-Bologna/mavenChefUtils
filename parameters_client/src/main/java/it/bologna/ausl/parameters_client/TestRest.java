package it.bologna.ausl.parameters_client;

import java.util.List;
import java.util.Map;

/**
 *
 * @author gdm
 */
public class TestRest {
    public static void main(String[] args){
        try {
//            it.bologna.ausl.bdm.core.Step.StepLogic.valueOf("");
//            System.out.println(it.bologna.ausl.bdm.core.BdmProcess.ProcessDirection.valueOf("BACKWARD").name());
//            System.out.println(it.bologna.ausl.bdm.core.BdmProcess.ProcessDirection.valueOf("FORWARD").name());
//            System.exit(0);
//            RemoteBdmClientImplementation remoteBdmClientImplementarion = new RemoteBdmClientImplementation("https://user:siamofreschi@gdml:10000/");
            ParametersRestClient client = new ParametersRestClient();
//            BalboClient client = new BalboClient("http://localhost:8080/");
//            List<Map<String, String>> completeParameters = client.getCompleteParameters();
//            for (Map<String, String> p: completeParameters) {
//                System.out.println(p);
//            }
//            System.out.println(client.getCompleteParameter("generateNumberRetryTime"));

            System.out.println("----------------------------");
            Map<String, String> parameters = client.getParameters();
            System.out.println(parameters.get("bonitaUrl"));
           
            System.out.println(client.getParameter("bonitaUrl"));
            
           System.exit(0);
        } 
        catch (Exception ex) {
         ex.printStackTrace();
        }
        
    }
}
