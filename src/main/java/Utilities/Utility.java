package Utilities;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;

public class Utility {

        String propertyFileName = null;

        public String vehicleNumber;

        public static String generateRandomVehicleNumber() {
            return null;
        }


        /*==============================================================================================
            These method are for reading property file
           ================================================================================================*/
        public void setPropertyFile(String propertyFileName) {
            this.propertyFileName = propertyFileName;
        }

        public String gettingValueOfProperty(String property) throws Exception {
            Properties prop = new Properties();
            InputStream input = null;
            String requiredPropertyValue = null;
            try {
                input = new FileInputStream(propertyFileName);
                // load a properties file
                prop.load(input);
                // get the property value
                requiredPropertyValue = prop.getProperty(property);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (input != null) {
                    input.close();
                }

            }
            return requiredPropertyValue;

        }



        public static class RandomVehicleNumberGenerator {

            public String generateRandomVehicleNumber() {
                // Generate a random number between 10 and 99 for the first two digits
                int firstTwoDigits = generateRandomNumberInRange(10, 99);

                // Generate three random uppercase letters for the next three characters
                String letters = generateRandomLetters(2);

                // Generate a random number between 1000 and 9999 for the last four digits
                int lastFourDigits = generateRandomNumberInRange(1000, 9999);

                // Combine the generated parts to form the vehicle number
                String vehicleNumber = "HR" + firstTwoDigits + letters + lastFourDigits;

                return vehicleNumber;
            }

            public int generateRandomNumberInRange(int min, int max) {
                Random random = new Random();
                return random.nextInt(max - min + 1) + min;
            }

            public String generateRandomLetters(int count) {
                StringBuilder sb = new StringBuilder();
                Random random = new Random();
                for (int i = 0; i < count; i++) {
                    char letter = (char) (random.nextInt(26) + 'A');
                    sb.append(letter);
                }
                return sb.toString();
            }

        }


}


