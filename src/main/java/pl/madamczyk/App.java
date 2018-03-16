package pl.madamczyk;

import pl.smsapi.BasicAuthClient;
import pl.smsapi.api.SmsFactory;
import pl.smsapi.api.action.sms.SMSSend;
import pl.smsapi.api.response.MessageResponse;
import pl.smsapi.api.response.StatusResponse;
import pl.smsapi.exception.ClientException;
import pl.smsapi.exception.SmsapiException;


public class App {
    public static void main(String[] args) {
        try {
            String passwordHash = "dcc557a6a1768bb676f7dcdf42b2bce9";
            BasicAuthClient client = new BasicAuthClient("kontakt@mryndak.pl", passwordHash);
            Commander commander = new Commander();

            commander.start();
            SmsFactory smsApi = new SmsFactory(client);
            String phoneNumber = Commander.getPhoneNumber();
            SMSSend action = smsApi.actionSend()
                    .setText(Commander.getMessage())
                    .setTo(phoneNumber);

            StatusResponse result = action.execute();
            commander.finish();
            for (MessageResponse status : result.getList() ) {
                System.out.println(status.getNumber() + " " + status.getStatus());
            }
        } catch (ClientException e) {
            /**
             * 101 Niepoprawne lub brak danych autoryzacji.
             * 102 Nieprawidłowy login lub hasło
             * 103 Brak punków dla tego użytkownika
             * 105 Błędny adres IP
             * 110 Usługa nie jest dostępna na danym koncie
             * 1000 Akcja dostępna tylko dla użytkownika głównego
             * 1001 Nieprawidłowa akcja
             */
            e.printStackTrace();
        } catch (SmsapiException e) {
            e.printStackTrace();
        }
    }
}
