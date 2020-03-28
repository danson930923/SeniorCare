package seniorcare.db.cosmos;

import org.apache.commons.lang3.StringUtils;

/**
 * Contains the account configurations for Sample.
 *
 * For running tests, you can pass a customized endpoint configuration in one of the following
 * ways:
 * <ul>
 * <li>-DACCOUNT_KEY="[your-key]" -ACCOUNT_HOST="[your-endpoint]" as JVM
 * command-line option.</li>
 * <li>You can set ACCOUNT_KEY and ACCOUNT_HOST as environment variables.</li>
 * </ul>
 *
 * If none of the above is set, emulator endpoint will be used.
 * Emulator http cert is self signed. If you are using emulator,
 * make sure emulator https certificate is imported
 * to java trusted cert store:
 * https://docs.microsoft.com/en-us/azure/cosmos-db/local-emulator-export-ssl-certificates
 */
public class AccountSettings {
    // Replace MASTER_KEY and HOST with values from your Azure Cosmos DB account.
    // The default values are credentials of the local emulator, which are not used in any production environment.
    // <!--[SuppressMessage("Microsoft.Security", "CS002:SecretInNextLine")]-->
    public static String MASTER_KEY =
            System.getProperty("ACCOUNT_KEY",
                    StringUtils.defaultString(StringUtils.trimToNull(
                            System.getenv().get("ACCOUNT_KEY")),
                            "4H30cRVJ4YaaNXRHpdZY013whJDb2lZXW7SBDnYAISe9bZFy76g3w3QnN926r7FztJAasDoBQ8mvBD7u0bvn3A=="));

    public static String HOST =
            System.getProperty("ACCOUNT_HOST",
                    StringUtils.defaultString(StringUtils.trimToNull(
                            System.getenv().get("ACCOUNT_HOST")),
                            "https://android-health-helper.documents.azure.com:443/"));

    public static String DATABASE_NAME =
            System.getProperty("DATABASE_NAME",
                    StringUtils.defaultString(StringUtils.trimToNull(
                            System.getenv().get("DATABASE_NAME")),
                            "SeniorCare"));


}
