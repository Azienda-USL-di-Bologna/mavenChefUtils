package it.bologna.ausl.parameters_client;

import java.util.List;
import java.util.Map;

/**
 *
 * @author gdm
 */
public class TestDb {

    public static void main(String[] args) {
        try {
//            it.bologna.ausl.bdm.core.Step.StepLogic.valueOf("");
//            System.out.println(it.bologna.ausl.bdm.core.BdmProcess.ProcessDirection.valueOf("BACKWARD").name());
//            System.out.println(it.bologna.ausl.bdm.core.BdmProcess.ProcessDirection.valueOf("FORWARD").name());
//            System.exit(0);
//            RemoteBdmClientImplementation remoteBdmClientImplementarion = new RemoteBdmClientImplementation("https://user:siamofreschi@gdml:10000/");
            ParametersClient client = new ParametersDbClient();
//            System.out.println(client.getRawParameters().toString());
//            System.out.println(client.getParameter(("serverAllaCazzo")));
//            if (true) return;
//            System.out.println(client.getParameter("descrizioneDocumentoTemplate"));
//            System.out.println(client.getParameter("MittenteAOOCodice"));
//            System.out.println(client.getParameter("parerStrutturaVersante"));
//            if (true) return;
////            List<Map<String, String>> completeParameters = client.getCompleteParameters();
////            for (Map<String, String> p : completeParameters) {
////                System.out.println(p);
////            }
////            if (true) {
////                return;
////            }
            System.out.println(client.getAmbiente());
            System.out.println(client.getAzienda());
            System.out.println(client.getEncryptionKey());
            client.getCompleteParameters();
            System.exit(0);
//            System.out.println(client.getCompleteParameter("generateNumberRetryTime"));
//
            System.out.println("----------------------------");
            Map<String, String> parameters = client.getParameters();
            System.out.println(parameters);

            for (int i = 0; i < 10000; i++) {
                System.out.println(client.getParameter("UuidBonitaSmistamento"));
                byte[] read = new byte[100];
                System.in.read(read);
            }
            System.exit(0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
