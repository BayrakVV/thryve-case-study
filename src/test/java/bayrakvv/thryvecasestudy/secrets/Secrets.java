package bayrakvv.thryvecasestudy.secrets;

import io.github.cdimascio.dotenv.Dotenv;

public class Secrets {
    private static final Dotenv env = Dotenv.configure().ignoreIfMissing().load();
    public static final String THRYVE_API_BASE_URL = env.get("THRYVE_API_BASE_URL");
    public static final String PUTS_REQ_URL = env.get("PUTS_REQ_URL");
    public static final String USERNAME = env.get("USERNAME");
    public static final String PASSWORD = env.get("PASSWORD");
    public static final String APP_AUTH_ID = env.get("APP_AUTH_ID");
    public static final String APP_AUTH_SECRET = env.get("APP_AUTH_SECRET");
}
