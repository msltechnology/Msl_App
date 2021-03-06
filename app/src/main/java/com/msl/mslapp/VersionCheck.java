package com.msl.mslapp;

public class VersionCheck {

    // 해당 클래스는 버젼 별 및 업데이트 관리를 적기 위해 만든 것, 코드는 안적을 예정

    // 이전까지 블루투스 5.0도 검색하기 위해 블루투스 스캔 예제 보고 만드는 중.
    // 김건우 제작
    // 2021-02-09 / 스캔 부분 만드는 중, 결과 예상 화면 짜기, 각 기능 별 공부할꺼 리스트 만듬, 한 화면에 fragment 넣을까 고민중
    // 2021-02-22 / 스캔 및 연결, 리드, 라이트 까지 가능. 말그대로 가능만하고 그외 받고 보낼껄 정리하고 화면꾸미고 데이터별로 처리는 아직.
    // 2021-02-24 / 기존 등명기 연결 안되던거 하게만듬. uuid 검색해서 맞는걸로 연결.
    // 2021-03-02 / 사이드바 완료 탭을 이용한 status, setting, test 등 기능 이용할 생각 중
    // 2021-03-03 / 탭을 만들었으나 fragment 화면을 frameLayout 에서 viewpage로 변경하니 불러오는 순서가 바껴서 status가 먼저나옴....머지?
    // 2021-03-05 / fragment 새로 만들어서 그 속에 viewpage넣고 처리함. 근대 현재 fragment에 데이터를 보내줘야하는데 viewpage라 옆 fragment값을 가져옴. viewpage2 사용권장함.
    // 2021-03-08 / Viewpage2 사용. 탭 및 각 프래그먼트 슬라이드 효과 추가. 허나 여전히 하위 프래그먼트와 연결안됨. getSupportFragmentManager가 작동안하는거 같은데... 다른방법이?
    // 2021-03-09 / FragmentManager를 getChildFragmentManager로 변경 하여 연결. bluetooth 5.0 데이터 수신 간 데이터 끝 CR 존재(줄바꿈) readData에서 CR or LF 제거하도록 함. 잘됨. 하위 프래그먼트 사용 가능!. 기타 로그 정리
    // 2021-03-10 / 로딩바 만들기 실패!
    // 2021-03-12 / 간단한 방법으로 로딩바 만듬.
    // 2021-03-15 / password 프래그먼트 만들어서 비밀번호 전송 요청 시 로딩바 사라지고, 해당 대답을 한 경우 다음으로 넘어 가서 기능 할 수 있도록 함. 비밀번호 디자인 등은 차후에...
    //            / RTU 제작 중. usb 연결 다른방법 시도하다 안되서 기존방법 다시 시도 중. d2xx 및 pl2303 임포트 성공.
    // 2021-03-17 / rtu 통신 시도 중. d2xx 및 pl2303 어찌 사용하는지 감이 안잡히고 다른 방법으로도 안됌. 기존 어플에서 차용하여 사용할려함. 아직 모르겠음.
    // 2021-03-17 / rtu 통신 성공. pl2303은 안사용해도 되는거였음(괞이 같이 쓰느라 더 꼬인 기분). d2xx로만 이용하여서 통신 성공 및 들어오는 데이터 구분하여 데이터 배분도 완료. 이후 layout에 맞춰서 설정만 하면 될듯.
    //              attach 했을 때 어플 키게하는거 없애게 할 예정(왜뜨는겨?)
    // 2021-03-22 / 현재 기준 보급으로 나눠드린 핸드폰 LG X4로는 장거리 통신(long range)이 안되므로 최신 기종인 S10 5G사용 결과 잡힘. 허나 최신 기종 사용 시 위치권한 얻는게 불안정하고 통신 간 데이터 또한 불안전함. 일단 불안전 잡고 롱렌지 시도
    // 2021-03-22 / 불안전 잡은듯(확신은 못함).
    // 2021-03-25 / long range 블루투스가 검색이 안됌(타사 블루투스 검색 앱으로는 되는걸 봐서는 앱에서 못잡는듯). 해당 사항에 대해 공부중...뭐가문제지
    // 2021-03-26 / setting 값 등 변경하였으나 안됌. 따로 조치를 해야하는듯. 대신 세팅값 바꿔서 스캔하는 능력은 증가시킨대신 배터리 많이 쓰는 모드로 사용.
    // 2021-03-29 / 패스워드 키보드 등 값 입력 및 암호화(맞나?) 작업 함. 기존 시스템을 그대로 이용해야해서 손댈 수 가 없음. 드디어 setting 값에 legacy 값 변경으로 advertising 검색가능함. 대신 들어오는 스캔 값(scanresult)의 변화로 읽어오는 값 확인해야함.
    // 2021-03-30 / github 와 연동, 언어 별 string 설정 해야할 필요, ble_setting 에서 dialog 프래그먼트 사용할 예정. ble beginning 부분 bluetooth 도중에 꺼지거나 하면 체크할 수 있도록 수정해야함.
    // 2021-04-01 / fragment_setting 의 dialog 어떻게 만들지 및 만드는중. listview 가 문제...
    // 2021-04-05 / setting 관련 dialog 대략 틀 만듬. LG x4가 bluetooth advertising 인식 못하는게 대부분 최신폰 아니면 인식 못함. 삼성 S10 이후 부터 가능. 아이폰도 안됨. 할려면 중국폰(하웨이, 원플러스, 샤오미 등)이나 삼성폰인데 어차피 둘다 비쌈(60만원이상)
    // 2021-04-06 / beginning의 권한 확인 할려고 main 권한 확인 static 작업 중. 할까 말까.
    // 2021-04-07 / beginning의 버튼 누를 시 bluetooth 켜져있는지 상태 받아서 그다음 넘어 갈지 말지 설정. Bluetooth 온오프 간 해당 플래그 값 변경되게 함.
    // 2021-04-09 / setting dialog 만드는중. 각 초, 섬광 누를 시 표 등장하여 해당 값 누를 시 해당 값 버튼에 표시, 이후 검색 누를 시 해당 값에 맞는 리스트가 뜨도록 할 예정.(많은 노가다가 될꺼 같음).
    //              초 관련 fragment는 weight 항목을 많이 넣어서 그런지 로딩 시간이 길다. list쪽은 tablelayout으로 할 예정. 더불어 password쪽도 table로 바꿀까 생각 중.(weight 항목이 많아서 켜지는데 시간 걸림)
    // 2021-04-12 / setting dialog list tablelayout 만들었으며 각 선택한 등질 및 초 에 맞게 검색되게함(구현만하고 다 추가하지는 않음. 추가중). 등질에서 특수 등질도 선택하게함.
    // 2021-04-13 / 각 선택한 값에 맞춰 다 되게하였으며 초 선택 시 해당 초값을 가진 섬광 구별시킴. 섬광먼저 선택 시는 내일 할 예정.
    // 2021-04-14 / 섬광 선택했을 경우까지하면 어느것을 선택하든 하얀글자가 존재해 헷갈릴 수 있으므로 초만 함. 초와 섬광값을 선택 시 자동으로 검색하여 검색 버튼 삭제. 해당 등질 선택 후 확인 시 setting 화면에 값 표기, ID dialog 또한 설정함.
    // 2021-04-19 / layout 정리, 한/영 변환 기능 추가, 뒤로가기 기능 추가(이전 화면으로 이동)
    // 2021-04-20 / 언어 변환 마무리 및 패스워드 변경 추가, 색상 변경 및 layout 변경 중.
    // 2021-04-22 / layout 정리, 자동요청 기능, RTU 통신안되던거 다시 하게함(왜 안됬는지는 몰것음..), 배터리 디테일 다이어로그 추가 중.
    // 2021-04-26 / RTU 새로운 연결 케이블 용으로 RTU 뜯어고치는 중. 연결 및 데이터 받기가 가능하나 버튼 눌러야 가능하므로 자동으로 가능하게 handler 통해 할려는데 먼가 이상해짐(재대로 다시 봐야할듯), 보내는 것도 잘 되는지는 받기 이후 데이터 요청 등을 통해 실험할 예정.
    // 2021-04-27 / RTU 새로운 시리얼 통신 성공, 그 외 layout 등 변경중. 기존 RTU 앱으로도 이식 중
    // 2021-04-29 / RTU layout 설정 및 setting 관련 쪽도 설정 완료. 기존 RTU 앱도 변경 완료.(플레이 스토어에 적용), 메뉴얼 틀 만듬(후에 이미지 및 설명 넣어야함)
    // 2021-04-30 / log 기능 만듬(아직 모든 코드에 적용은 못함), navigation 을 custom 시킴.
    // 2021-05-04 / Log 기능 적용, Ble Setting 에 버전 확인 및 섬광 딜레이 기능 추가(단 최신버전 등명기에만 적용됨)
    // 2021-05-07 / Log 기능 중 간혹 팅기는 현상 발생, 원인에 대한 해결 방안을 실시 하였으나 잘 안된다. list 를 notify 해서 adapter 로 변경하였으나 안됌.
    // 2021-05-17 / d2xx, pl2303 제거, Ble 스캔 간 신호세기 및 userdata 회신화 하도록함(등명기를 막 키더라도 loading 이후 해당 등명기의 번호가 보임), Bluetooth 위경도 값 변화, Ble Function에 RTU 관련 항목 추가

}
