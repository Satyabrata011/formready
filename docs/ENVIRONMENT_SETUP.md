# Manual environment setup — Windows

Follow this guide in order. Close and reopen PowerShell or IntelliJ after changing environment variables.

## Verified starting point

| Tool | Status | Notes |
| --- | --- | --- |
| Windows | Ready | Development is being performed from PowerShell on Windows. |
| JDK | Installed | Java and compiler version `21.0.1`; JDK path is `C:\Program Files\Java\jdk-21`. |
| `JAVA_HOME` | Needs configuration | Maven cannot run until it is set. |
| Git | Installed | Version `2.43.0.windows.1`. |
| IntelliJ IDEA | Installed | Version `2026.1.3`. |
| Google Chrome | Installed | Version `154.0.8037.97`. |
| Maven CLI | Needs installation | IntelliJ has a bundled Maven, but a separate Maven installation is recommended. |
| Apache JMeter | Needs installation | Required later for API baseline checks. |
| Docker Desktop | Not installed | Optional until database container/deployment work. |

## 1. Set `JAVA_HOME`

1. Press the Windows key and search for **Edit the system environment variables**.
2. Open it, then select **Environment Variables…**.
3. Under **User variables**, select **New…**.
4. Enter:

   ```text
   Variable name: JAVA_HOME
   Variable value: C:\Program Files\Java\jdk-21
   ```

5. Under **User variables**, select `Path`, then **Edit**.
6. Select **New** and add:

   ```text
   %JAVA_HOME%\bin
   ```

7. Select **OK** on every open dialog.
8. Open a *new* PowerShell window and run:

   ```powershell
   $env:JAVA_HOME
   java -version
   javac -version
   ```

Expected result: the first command prints `C:\Program Files\Java\jdk-21`, and both Java commands report version 21.

## 2. Install Apache Maven

1. Open the official Maven page: <https://maven.apache.org/download.cgi>.
2. Download the **Binary zip archive** for the current stable Maven 3 release. At planning time, the page lists Maven `3.9.16`; if the page lists a newer stable 3.x release, select that instead.
3. Create this folder in File Explorer:

   ```text
   C:\dev-tools
   ```

4. Extract the downloaded archive into `C:\dev-tools`.
5. The final folder should resemble:

   ```text
   C:\dev-tools\apache-maven-3.9.16
   ```

6. Return to **Environment Variables**. Under **User variables**, create:

   ```text
   Variable name: MAVEN_HOME
   Variable value: C:\dev-tools\apache-maven-3.9.16
   ```

   Use the actual folder name if a newer Maven version was downloaded.

7. Edit the user `Path` variable and add:

   ```text
   %MAVEN_HOME%\bin
   ```

8. Open a new PowerShell window and run:

   ```powershell
   mvn -version
   ```

Expected result: Maven version information appears and its Java home points to the JDK 21 installation.

## 3. Install Apache JMeter

1. Open the official JMeter page: <https://jmeter.apache.org/download_jmeter.cgi>.
2. Download the **ZIP** binary release. At planning time, the page lists JMeter `5.6.3`; select a newer stable version if available.
3. Extract it to:

   ```text
   C:\dev-tools\apache-jmeter-5.6.3
   ```

4. Optionally add the following user variable:

   ```text
   Variable name: JMETER_HOME
   Variable value: C:\dev-tools\apache-jmeter-5.6.3
   ```

5. Optionally add `%JMETER_HOME%\bin` to the user `Path`. This lets you start JMeter from PowerShell.
6. Start JMeter by double-clicking:

   ```text
   C:\dev-tools\apache-jmeter-5.6.3\bin\jmeter.bat
   ```

   Or, if you added it to `Path`, run:

   ```powershell
   jmeter
   ```

Expected result: the Apache JMeter window opens without a Java error.

## 4. Configure Git identity

Use the name and email address you want shown on GitHub commits:

```powershell
git config --global user.name "Your Name"
git config --global user.email "your-github-email@example.com"
git config --global --list
```

Do not use an email address you do not want public in repository commit history.

## 5. Configure IntelliJ IDEA

1. Open IntelliJ IDEA.
2. Select **File → New → Project** when we create the application.
3. Choose **Maven** and select JDK `21` as the project JDK.
4. In **Settings → Build, Execution, Deployment → Build Tools → Maven**, use the Maven installation from `C:\dev-tools` after Step 2 is complete. IntelliJ’s bundled Maven is an acceptable temporary fallback.

## 6. Confirm Chrome and Selenium readiness

Chrome is already installed. Do **not** manually download ChromeDriver. We will use Selenium Manager, which obtains a compatible driver automatically when the UI test framework is created.

## 7. Defer Docker Desktop

Docker is optional now. Install it later, when we need PostgreSQL containers or consistent deployment environments.

Official download: <https://www.docker.com/products/docker-desktop/>.

Do not install it yet unless you are comfortable enabling WSL 2 and virtualization on Windows.

## Completion checklist

- [ ] `JAVA_HOME` is set and Java 21 works in a new PowerShell window.
- [ ] `mvn -version` succeeds.
- [ ] JMeter opens successfully.
- [ ] Git user name and email are configured.
- [ ] IntelliJ recognizes JDK 21 and the external Maven installation.
- [ ] Docker is intentionally deferred.

