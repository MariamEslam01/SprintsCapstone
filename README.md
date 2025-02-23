# SprintsCapstone
DemoBlazeAutomation

Overview
DemoBlazeAutomation is a Test Automation Framework designed to automate test cases for the DemoBlaze web application. It is built using:
- Selenium WebDriver for browser automation.
- TestNG as the testing framework.
- Maven for dependency management and build automation.


Functionality
- **User Registration**: Automates the sign-up process to validate user creation.
- **User Login**: Ensures users can log in with valid credentials.
- **Logout**: Verifies that users can successfully log out.
- **Shopping Cart Operations**: Tests adding and removing items from the cart.

Prerequisites
Before running the tests, ensure that the following dependencies are installed:

- Java JDK 8+ (Download: https://www.oracle.com/java/technologies/javase-downloads.html)
- Maven (Download & Setup: https://maven.apache.org/install.html)
- Chrome WebDriver or GeckoDriver (Download: https://www.selenium.dev/documentation/webdriver/getting_started/install_drivers/)

Installation & Setup
1. Clone the repository:
   git clone <repository-url>
   cd DemoBlazeAutomation

2. Install dependencies using Maven:
   mvn clean install

3. Run the TestNG suite:
   mvn test

Running Tests
To execute all test cases:
   mvn test

To run a specific test suite (e.g., testng.xml):
   mvn test -DsuiteXmlFile=testng.xml

Dependencies (From pom.xml)
- Selenium WebDriver
- TestNG
- Maven Surefire Plugin (for executing tests)
