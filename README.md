# 목표 리마인더: 당신의 목표를 잠금해제</br>(Goal Reminder: Unlock Your Goals)
휴대폰을 켤 때마다 목표를 리마인드하고, 목표를 작게 분할하여 정복한다.</br>
단순한 투두리스트가 아닌 '왜' 하는지 아는 투두가 된다.

<img src="https://github.com/nosorae/Goal-Reminder-Unlock-Your-Goals/assets/62280009/3d341cb2-50b1-4028-902a-2c262aabe370" width="100%" height="0%"/></br>
[[구글플레이에서 보기]](https://play.google.com/store/apps/details?id=com.yessorae.goalreminder)</br>
[[개인정보 처리방침]](https://www.notion.so/6b0ca10f3abc438783b29b48169cdd48)

## 소개
### 제작자
1인 기획/디자인/개발

### 제작 동기
제가 좌절할 때 받은 조언을 바탕으로 만들게 되었습니다.</br>
저는 함께 일하고 싶은 좋은 개발자가 되고 싶고 돈도 많이 벌고 싶은 욕구가 있었습니다.</br>
하지만 하루하루를 나름 열심히 산다고 하지만 가시적 성장이 나오지 않으니 불안하고 조급해지고 좌절하기도 했습니다.</br>
그런 중에 '1년 목표, 1개월 목표, 1주 목표, 오늘 할 일' 이렇게 큰 목표를 설정하고 하루 안에 할 수 있는 작은 목표로 까지 나누어 생각해보라는 조언을 받았습니다.</br>
그리고 조언을 자주 확인하라는 조언도 받았습니다. 목표를 잊으면 또 다시 길을 잃기 쉽상이니까요.</br>
아무도 다운받지 않아도 제가 쓰면 좋겠다 싶었습니다.

### 특징
1. N년 후 목표, 월별 목표, 주간 목표, 오늘 할 일로 탭이 나누어져 크기별로 목표를 관리할 수 있어요.</br>
2. 목표를 연결하고 달성도를 체크할 수 있어요. 예를 들어 1개월짜리 목표는 1년 목표의 하위목표가 될 수 있고 1개월 짜리가 달성되었을 때 기여점수를 매길 수 있습니다.</br>
3. 잊지 않게 잠금해제할 때마다 자동으로 앱이 실행되어있어요.

## 개발
### 기술적인 동기
만들 당시 Clean Architecture에 대해 관심을 가지고 공부하고 있었습니다.</br>
공부만 하고 끝내기는 아쉬워서 Clean Architecture를 실제 서비스에 구현하기로 했습니다.</br>
또한 의존성을 명확히 하기위해 아키텍쳐 수준에서의 Multi Module을 적용하게 되었습니다.</br>
UI는 생산성을 높이기 위해 Jetpack Compose를 사용하고 디자인을 따로하지 않고 MDC3 컴포넌트를 조합하여 만들었습니다.

### 언어 
  - Kotlin
### 주요 기술
  - UI 개발: Compose
  - 비동기 작업: Cotourine/Flow
  - 의존성 주입: Hilt
  - 백그라운드 동작: WorkManager, Service + BroadcastReceiver
  - 로컬 데이터 저장: Room, Datastore-Preference

## 아키텍처
### Clean Architecture
![mobile_clean_architecture_goal_reminder](https://github.com/nosorae/Goal-Reminder-Unlock-Your-Goals/assets/62280009/cbde61d2-5120-43ad-982c-4a6ccbfae9ec)

### 의존성 그래프
![project-dependency-graph](https://github.com/nosorae/Goal-Reminder-Unlock-Your-Goals/assets/62280009/79c8ed6e-a766-45dc-af78-c28d35686641)</br>
(made by [gradle-dependency-graph-generator-plugin](https://github.com/vanniktech/gradle-dependency-graph-generator-plugin))

- UI레이어
  - 단방향 데이터 흐름(UDF)으로 구성되어있습니다. 상태가 UI를 그리고 UI에서 발생한 이벤트가 상태롤 변경시킵니다.
  - MVVM 패턴으로 구현하였습니다.
  - Jetpack Compose 를 사용하였습니다.
- Domain레이어는 안드로이드 프레임워크에 의존하지 않습니다.
- Data레이어에서 데이터소스는 로컬(DB, Preference)데이터입니다. 현재 로컬만 지원합니다.

## 디자인
- 요구사항과 관련된 레퍼런스를 찾아 어떻게 배치할지 정도만 정했습니다.
- 그 후 Material Design Components를 조합하여 만들었습니다. 주로 참고한 문서는 아래와 같습니다.
  -  [MDC3 문서](https://m3.material.io/components)
  -  Figma Community의 [Material 3 Design Kit](https://www.figma.com/file/KUN5AFovqZoflSJQTj4Wlk/Material-3-Design-Kit-(Community)?type=design&node-id=51964-62981&mode=design)

## 회고. (기획 보강 후 진행 예정)
### 서비스 측면
결론적으로 제가 사용하지 않고 있습니다..</br>
몇 가지 개선해야할 점이 있습니다.</br>
- 다른 디바이스/서비스와 동기화가 되었으면 좋겠다.
  - 모바일로 큰 목표를 작은 목표로 잘게 나누며 입력하는 건 꽤나 고된 일이었습니다.
  - 서버를 개발하여 연동하거나 구글캘린더 같은 다른 일정앱과 연동시키거나 하는 방식으로 해결하면 좋겠습니다.
- 투두/목표 순서 바꿀 수 있으면 좋겠다.
  - 지금은 입력한 순서대로 나오고 있는데 순서를 바꾸는 기능을 넣으면 좋겠다.
- 월/주간 목표의 날짜 선택이 어렵다. 월선택할 때 날짜까지 선택해야하는 불필요한 과정이 있다.

### 기술 측면
테스트 코드를 작성하고 싶다.</br>
정책이 헷갈리는 부분이 많다. 기여도를 계산하는 데 실수가 일어날 부분도 많다.</br>
테스트 코드를 작성하여 사이드 이펙트를 방지하면 더 자신감을 가지고 새로운 기능을 붙일 수 있을 것 같다.</br>
그리고 좋은 아키텍처의 이점을 온전히 느끼기 위해서도 테스트코드를 작성하기 용이한지 확인하고 싶다.</br>
Github Action 으로 PR시 체크하면 더 편하고 안전하게 개발할 수 있을 것 같댜.
