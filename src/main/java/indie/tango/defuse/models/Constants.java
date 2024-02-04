package indie.tango.defuse.models;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Constants {

    public static class Scenario {
        public List<String> steps;

        public Scenario(String... steps) {
            this.steps = Arrays.asList(steps);
        }
    }

    public static final String switchButton = "switch";
    public static final String greenButton = "greenButton";
    public static final String blueButton = "blueButton";
    public static final String redButton = "redButton";
    public static final String yellowButton = "yellowButton";
    public static final String greenWire = "greenWire";
    public static final String blueWire = "blueWire";
    public static final String redWire = "redWire";
    public static final String yellowWire = "yellowWire";

    public static List<Scenario> scenarios = Arrays.asList(
            new Scenario(redButton, blueButton, switchButton, greenWire),
            new Scenario(yellowWire, greenButton, redButton, greenWire),
            new Scenario(blueWire, yellowButton, greenWire, redWire),
            new Scenario(greenButton, switchButton, blueWire, yellowWire),
            new Scenario(switchButton, yellowWire, yellowButton, redButton),
            new Scenario(blueWire, greenButton, redButton, yellowWire),
            new Scenario(redWire, redButton, greenWire, switchButton),
            new Scenario(blueWire, redWire, greenButton, yellowWire),
            new Scenario(yellowButton, switchButton, blueButton, greenWire),
            new Scenario(blueWire, yellowButton, redWire, greenButton)
    );

    public static List<String> imgId = Arrays.asList(
            "1ixoue0PeBzQMXUKaSR4_UoYcOEf0z0H4",
            "15sw7ft2ORowsLtqG2kCSPclzOsxlg0CZ",
            "1Bnal3K9joINJJNsZJqm2MHvH9EFTCCob",
            "Dw0VzyxDa2eSJQXcLkCB0fE0xDSeFZzt",
            "1ugcpFBhJi_IBoyK87OAY0t5WYxxIGpds",
            "1qLG4gItga-ekKa04QjrfKsY9qrSl4ARW",
            "17oU_kF4W6BYq5DFoS73jvUHaJEi4VdnC",
            "1fnhAwME6fwvnF6URW7DEtu8wBmtxgV6y",
            "1B3D5j0a3F5i-soJkGJJ49LUmJrCLIULM",
            "1KuW7sPbfo6u-OjggNm9jHplXXeaxw0gD"
    );
}
