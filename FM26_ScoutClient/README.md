# FootballScoutAI: Client desktop

Aplicație desktop pentru Windows, realizată în **C#** cu **WPF** pe **.NET 8**. Se conectează la backend-ul Spring Boot și permite consultarea datelor de scouting direct de pe desktop.


## Cerințe

- Windows 10 sau 11
- [.NET 8 SDK](https://dotnet.microsoft.com/download/dotnet/8.0)
- Visual Studio 2022 (recomandat) sau `dotnet` CLI
- Backend-ul pornit (vezi `../demo/README.md`)

## Rulare

**Din Visual Studio:** deschide `FM26_ScoutClient.sln` și apasă `F5`.

**Din linia de comandă:**

```bash
cd FM26_ScoutClient/FM26_ScoutClient
dotnet run
```

## Configurare

Clientul trebuie să cunoască adresa backend-ului (implicit `http://localhost:8080`). Verifică URL-ul folosit în `MainWindow.xaml.cs`.

## Build

```bash
dotnet build -c Release
```

Executabilul rezultă în `bin/Release/net8.0-windows/`.

## Tehnologii

- C#
- WPF
- .NET 8
