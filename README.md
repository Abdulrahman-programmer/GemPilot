# GemPilot

GemPilot helps developers explore and understand a codebase by connecting to GitHub, indexing repository content, and answering questions with source-backed citations. It turns a repo into a searchable, context-aware knowledge base you can query from a browser.

## What It Does

- Sign in with GitHub and work with repositories, including private repos permitted by your OAuth scopes.
- Index repository files into PostgreSQL using pgvector embeddings for semantic retrieval.
- Ask repository-specific questions in chat sessions and trace answers back to the exact file and lines that support them.
- Monitor indexing progress, errors, and repository status from a dashboard.

## Tech Stack

- Frontend: Next.js 16, React 19, TypeScript, and Tailwind CSS
- Backend: Java 25, Spring Boot, Spring AI, and Maven
- Database: PostgreSQL with the pgvector extension
- Integrations: GitHub OAuth and Google GenAI for embeddings and chat

## Requirements

Before running the app locally, make sure you have:

- Node.js and npm
- JDK 25
- Docker Compose
- GitHub OAuth app credentials with `read:user` and `repo` scopes
- A Google GenAI API key with access to the configured chat and embedding models

## Local Setup

Run the following commands from the repository root, which contains `docker-compose.yml`.

### 1) Start PostgreSQL

```bash
docker compose up -d postgres
```

The default local database settings are:

- Database: `gempilot`
- Username: `gempilot`
- Password: `postgres`
- Port: `5432`

### 2) Configure backend environment variables

Set the required credentials in the shell where you will run the backend. Do not commit real secrets to source control.

```bash
export GEMINI_API_KEY="your-google-genai-key"
export GITHUB_CLIENT_ID="your-github-client-id"
export GITHUB_CLIENT_SECRET="your-github-client-secret"
```

For local development, the backend defaults to `http://localhost:3000` for the frontend and allows that origin through CORS. If your frontend runs elsewhere, set `FRONTEND_URL` and `CORS_ORIGINS` to match.

### 3) Start the backend

```bash
cd backend
./mvnw spring-boot:run
```

The API is available at `http://localhost:8080` by default.

### 4) Start the frontend

Open a second terminal and run:

```bash
cd client
npm ci
npm run dev
```

Then open `http://localhost:3000` in your browser. The frontend uses `http://localhost:8080` as its API base by default. If needed, set `NEXT_PUBLIC_API_BASE_URL` before starting Next.js.

## Configuration

The backend reads standard Spring environment-variable overrides. Common settings are listed below:

| Variable | Default | Purpose |
| --- | --- | --- |
| `DB_URL` | `jdbc:postgresql://localhost:5432/gempilot` | PostgreSQL JDBC URL |
| `DB_USER` | `gempilot` | Database username |
| `DB_PASS` | `postgres` | Database password |
| `GEMINI_API_KEY` | Set in environment | Google GenAI chat API key |
| `GEMINI_EMBEDDING_KEY` | Set in environment | Google GenAI embedding API key |
| `GITHUB_CLIENT_ID` | Set in environment | GitHub OAuth client ID |
| `GITHUB_CLIENT_SECRET` | Set in environment | GitHub OAuth client secret |
| `FRONTEND_URL` | `http://localhost:3000` | Frontend URL used by backend auth redirects |
| `CORS_ORIGINS` | `http://localhost:3000` | Allowed browser origin for API requests |
| `NEXT_PUBLIC_API_BASE_URL` | `http://localhost:8080` | Backend URL used by the frontend |

The database and local frontend/backend URLs have sensible development defaults. OAuth credentials and Google GenAI keys are still required for sign-in, indexing, and chat to function correctly.

## Useful Commands

Run frontend checks from `client/`:

```bash
npm run lint
npm run build
```

Run backend tests from `backend/`:

```bash
./mvnw test
```

## Troubleshooting

- **Database connection fails:** Check that `docker compose ps` shows PostgreSQL running and that the backend's `DB_*` settings match the Compose configuration.
- **GitHub sign-in fails:** Verify the OAuth client credentials and confirm the GitHub OAuth app callback URL points to the backend's `/login/oauth2/code/github` endpoint.
- **The browser cannot reach the API:** Confirm the backend is running and that `NEXT_PUBLIC_API_BASE_URL`, `FRONTEND_URL`, and `CORS_ORIGINS` match your local URLs.
- **Indexing or chat fails:** Make sure both Google GenAI keys are valid and that the configured models are enabled for your API key.

## Project Layout

```text
backend/       Spring Boot API for GitHub integration, indexing, and chat streaming
client/        Next.js web application
docker/        PostgreSQL initialization scripts
docker-compose.yml
```

## Security Note

Keep GitHub and Google credentials, token-encryption settings, and production database passwords out of committed files. If credentials are already present in configuration, remove them, load secrets from environment variables or a secret manager, and rotate anything that may have been exposed.
