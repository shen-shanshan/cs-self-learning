# Cursor Usage

## Cursor Desktop

...

## Cursor CLI

- [Cursor CLI Docs](https://cursor.com/docs/cli/overview)
- [Cursor CLI Slash Commands](https://cursor.com/docs/cli/reference/slash-commands)

### Installation

```bash
# Install (macOS, Linux, WSL)
curl https://cursor.com/install -fsS | bash

# Install (Windows PowerShell)
irm 'https://cursor.com/install?win32=true' | iex

# Verify
agent --version

# Post-installation setup
echo 'export PATH="$HOME/.local/bin:$PATH"' >> ~/.bashrc
echo 'export PATH="/home/shahen/.local/bin:$PATH"' >> ~/.bashrc
echo 'export CURSOR_API_KEY=crsr_e7d0647f4ddd38061ec4bb0daebf9dacf65fb35e34d7d3e31303bea69c61858a' >> ~/.bashrc
source ~/.bashrc

# Run interactive session
agent

# Select a model
/model
```

### Authentication

Browser authentication (recommended):

```bash
# Log in using browser flow
agent login

# Check authentication status
agent status

# Log out and clear stored authentication
agent logout
```

API key authentication:

```bash
# API key name: cursor-amd
export CURSOR_API_KEY=crsr_e7d0647f4ddd38061ec4bb0daebf9dacf65fb35e34d7d3e31303bea69c61858a

# Login
agent "implement user authentication"

# Check your current authentication status
agent status
```

### Session Manage

```bash
# Open previous chats and resume one
agent ls

# Resume latest conversation
agent resume

# Continue the previous session
agent --continue

# Resume specific conversation
agent --resume="chat-id-here"
```

- `/summarize` free up space in the context window. `/compress` remains an alias.
- `/clear` Start a new chat session. `/new`, `/new-chat`, and `/newchat` are aliases.
- `/resume` Open recent chats and resume one.
- `/rewind` Jump back to a previous message.

### Shortcuts

- `Shift+Tab` — Rotate between modes (Agent, Plan, Ask)
- `Shift+Enter` — Insert a newline instead of submitting, making it easier to write multi-line prompts.
- `Ctrl+D` — Exit the CLI. Follows standard shell behavior, requiring a double-press to exit.

### Review

- Review changes with `Ctrl+R`.
- Press `i` to add follow-up instructions.

### Selecting Context

Select files and folders to include in context with `@`.

### CLI Worktrees

Pass `-w` or `--worktree [name]` to run the agent in a new Git worktree instead of editing your current checkout directly.

### Non-interactive Mode

Use `-p` or `--print` to run Agent in non-interactive mode. This will print the response to the console.

You can combine this with `--output-format` to control how the output is formatted. For example, use `--output-format json` for structured output that's easier to parse in scripts, or `--output-format text` for plain text output of the agent's final response.

### Shell Mode

Shell Mode runs shell commands directly from the CLI without leaving your conversation.

Use `/shell [command]` Enter Shell Mode. `/sh` and `/run` are aliases.

### Debug Mode

- `/debug [prompt]` Toggle Debug mode or submit a prompt in Debug mode.
- `/logs` Show the debug log path and copy it to the clipboard.

### Others

- `/status-indicators` Toggle terminal title status indicators.
- `/about` Show CLI version, system, and account info. Also copies it to the clipboard.
- `/vim` Toggle Vim keys.
- `/line-numbers` Toggle line numbers in code blocks.
- `/help [command]`	Show help for command details.
- `/plugin [subcommand]` Manage plugins and marketplaces.
- `/config` Configure CLI settings interactively.
- `/quit` or `/exit` Exit.
