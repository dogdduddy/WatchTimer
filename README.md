# WatchTimer
<div align="center">
  <img src="https://github.com/user-attachments/assets/30a4d01d-859e-49ce-a4b5-a24b6b9ab4b7" width="30%" />
  <img src="https://github.com/user-attachments/assets/95a5bf44-04bb-4134-a901-2a8609f9606d" width="30%" />
  <img src="https://github.com/user-attachments/assets/3d252646-9016-4ebb-b5d4-5bf818e399c0" width="30%" />
</div>

## **📌 프로젝트 소개**
WatchTimer는 사용자가 다양한 활동에 맞게 타이머를 설정하고 관리할 수 있는 애플리케이션입니다. 간단한 인터페이스와 다양한 기능을 통해 일상생활에서 시간을 효율적으로 관리할 수 있도록 도와줍니다.

- ⏱️ 타이머 설정 및 시작: 사용자가 원하는 시간으로 타이머를 설정하고 시작 가능합니다.
- 🔄 타이머 중단 및 재개 : 타이머를 중간에 중단하고, 다시 재개할 수 있습니다.
- 🔔 타이머 종료 알림 : 타이머 종료 시 진동으로 사용자에게 종료를 알립니다.
- 🏃 절전 모드 대응 : 갤럭시 워치의 절전 모드(Ambient Mode)에서도 타이머 동작을 지원합니다.
- ⚡ Foreground Service 지원 : 앱이 백그라운드에서도 타이머가 정상적으로 유지됩니다.

---

## **⚙️ 개발 환경 및 기술 스택**
- **개발 환경**: Android Studio, Kotlin, Compose
- **아키텍처**: MVI
- **비동기 처리**: Coroutine, Flow
- **DI**: Hilt

---

## **📂 프로젝트 구조**
Patata_AOS
```
├── app
├── model
│   ├── util
│   ├── extensions
├── navigation
├── presentation
│   ├── pickerscreen
│   ├── rotaryscreen
│   ├── timer
│   ├── viewmodel
├── utils
│   ├── TimerUtil
│   ├── VibrationHelper
│   ├── ViewExtenstion
├── service
```

---

## **📆 개발 기간**
📅 **2024-11-04 ~ 2024-12-14** (총 1개월)

---

## **🚀 기술적 도전 및 문제 해결**

1. MVI 패턴 학습 및 적용

	문제: 기존에는 UI 상태 관리를 위한 일관된 패턴이 부족했으며, 복잡한 상태 변화에 대응하기 어려웠음.
해결: MVI(Model-View-Intent) 아키텍처를 도입하여 UI 상태 관리를 일관성 있게 유지하고, 이벤트 기반으로 타이머 상태를 업데이트하도록 구성.

🔹 해결 과정
	•	StateFlow를 활용하여 UI 상태를 관리하고, 단방향 데이터 흐름을 유지.
	•	UI가 ViewModel의 상태를 구독하는 구조를 적용하여 상태 변경 시 불필요한 UI 업데이트를 방지.
	•	이벤트 기반으로 타이머 중단 및 재개를 효율적으로 처리하도록 구현.

✅ 결과
	•	UI와 데이터의 흐름이 더욱 직관적이 되었으며, 재현 가능한 상태 관리가 가능해짐.
	•	유지보수성과 확장성이 향상되어 새로운 기능 추가 시 구조적 일관성을 유지할 수 있음.

⸻

2. Compose 사용 및 리컴포지션 최소화를 위한 고민

	문제: Jetpack Compose를 활용하는 과정에서 불필요한 리컴포지션이 발생하여 성능 저하가 나타남.
해결: 리컴포지션을 최소화하기 위한 최적화 기법 적용.

🔹 해결 과정
	•	Layout Inspector를 활용하여 과도한 리컴포지션이 발생하는 부분을 분석.
	•	remember, rememberSaveable, derivedStateOf를 활용하여 불필요한 recomposition을 방지.
	•	State Hoisting을 적용하여 UI와 로직을 분리하고 상태 변경을 최소화.

✅ 결과
	•	불필요한 UI 업데이트를 방지하여 애니메이션 및 UI 렌더링 속도 개선.
	•	복잡한 상태 변화에도 부드러운 UI 경험을 제공할 수 있도록 최적화됨.

⸻

3. 서비스 기반 타이머 구현 및 진동 알림 최적화

	문제: 타이머가 백그라운드에서 유지되지 않으며, 사용자가 앱을 나가면 동작이 중단됨.
해결: Foreground Service를 활용하여 백그라운드에서도 타이머가 유지되도록 개선.

🔹 해결 과정
	•	Foreground Service를 활용하여 백그라운드에서 타이머가 지속 동작하도록 구현.
	•	Watch의 Vibrator API를 활용하여 타이머 종료 시 진동 패턴을 커스터마이징.
	•	사용자가 화면을 터치하지 않고도 타이머 종료를 직관적으로 감지할 수 있도록 추가적인 진동 패턴 적용.

✅ 결과
	•	타이머 종료 시 직관적인 알림을 제공하여 사용자 경험 향상.
	•	앱이 백그라운드 상태에서도 타이머가 중단되지 않고 정상적으로 동작하도록 개선됨.

⸻

4. 갤럭시 워치 절전 모드(Ambient Mode)에서 서비스 미동작 이슈 해결

	문제: 갤럭시 워치가 절전 모드(Ambient Mode)로 전환될 경우, 타이머 서비스가 정상적으로 동작하지 않는 문제가 발생함.
해결: Wake Lock을 활용하여 절전 모드에서도 타이머를 유지하는 방식으로 해결.

🔹 해결 과정
	1.	Wake Lock을 활용하여 CPU가 절전 모드에서도 타이머가 유지되도록 설정.
	2.	ViewModel에서 MutableLiveData 타입의 AmbientState를 생성하여 절전 모드 감지.
	3.	절전 모드에 진입한 후 일정 시간이 지나면 UI를 변경하여 사용자에게 표시.
	4.	사용자가 화면을 터치하면 절전 모드에서 해제되도록 구현.

✅ 결과
	•	앱이 절전 모드에서도 타이머를 정상적으로 유지할 수 있도록 개선됨.
	•	배터리 소모를 최소화하는 방식으로 최적화.
---
