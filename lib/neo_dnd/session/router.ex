defmodule NeoDnd.Session.Router do
  use GenServer

  import Ecto.Query, only: [from: 2]

  def start_link(session) do
    GenServer.start_link(__MODULE__, session, name: {:via, Registry, {NeoDnd.Session.Registry, session}})
  end

  @impl true
  def init(session) do
    query = from note in NeoDnd.Session.Note,
      where: note.session == ^session,
      select: %{id: note.id, content: note.content}

    {:ok, %{session: session, notes: NeoDnd.Repo.all(query)}}
  end

  @impl true
  def handle_call({:add_note, content}, _from, %{session: session, notes: notes} = state) do
    note = NeoDnd.Repo.insert!(%NeoDnd.Session.Note{session: session, content: content})
    note = Map.take(note, [:id, :content])

    {:reply, note, %{state | notes: [note | notes]}}
  end

  def handle_call(:list_notes, _from, %{notes: notes} = state) do
    {:reply, notes, state}
  end

  def add_note(pid, content) do
    GenServer.call(pid, {:add_note, content})
  end

  def list_notes(pid) do
    GenServer.call(pid, :list_notes)
  end
end
