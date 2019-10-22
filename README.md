# GargoyleWebDriver



###### IE11에 대한 웹 드라이버를 테스트하는데 발생한 이슈를 정리해본다.
  1. IEDriver가 필요하다. 아래 링크에서 사용하고자하는 버젼을 받도록한다. <br/>
  https://selenium-release.storage.googleapis.com/index.html
  
  2. 프로그램에서 InternetExplorerDriver 객체를 생성하기 전에 
  IEDriverServer.exe 파일을 System.setProperty를 통해 넣어줘야한다.
  
  3. 기본적으로 외부 접속이 불가하므로 몇가지 옵션을 추가해야한다.
  
    3-1. 프로그램에서 옵션 채택/
        
  ex) 
  HashMap<String, Object> capabilities = new HashMap<String,Object>();
			capabilities.put( InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			ImmutableCapabilities c = new ImmutableCapabilities(capabilities);
			InternetExplorerDriver iedriver = new InternetExplorerDriver(c);

    3-2. 레지스트리 등록 
      HKEY_CURRENT_USER\Software\Microsoft\Internet Explorer\Main 에 새 키 데이터를 추가하고 TabProcGrowth 값은 0을 넣어주어야함.



