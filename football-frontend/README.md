# FootballScoutAI: Frontend

Interfața web a aplicației FootballScoutAI, construită cu **Vue 3** și **Vite**. Comunică cu backend-ul Spring Boot printr-un API REST.

## Funcționalități

- Vizualizarea și adăugarea ligilor
- Vizualizarea și adăugarea echipelor
- Gestionarea jucătorilor (adăugare, editare, ștergere)
- Filtrarea jucătorilor
- Scouting asistat de AI

## Cerințe

- [Node.js](https://nodejs.org/) 18 sau mai nou
- Backend-ul pornit (vezi `../demo/README.md`)

## Instalare și rulare

```bash
npm install
npm run dev
```

Aplicația rulează implicit la `http://localhost:5173`.

## Configurare

Adresa backend-ului se setează în `src/services/api.js`. Asigură-te că URL-ul de bază corespunde portului pe care rulează backend-ul (implicit Spring Boot folosește `8080`).

## Build pentru producție

```bash
npm run build
npm run preview
```

Fișierele rezultate se află în folderul `dist/`.

## Tehnologii

- Vue 3
- Vite
- JavaScript
