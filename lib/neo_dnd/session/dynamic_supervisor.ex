defmodule NeoDnd.Session.DynamicSupervisor do
  use DynamicSupervisor

  def start_link(_) do
    DynamicSupervisor.start_link(__MODULE__, nil, name: __MODULE__)
  end

  def start_or_find_session(id) do
    DynamicSupervisor.start_child(__MODULE__, {NeoDnd.Session.Supervisor, id})
    [{pid, _}] = Registry.lookup(NeoDnd.Session.Registry, id)

    {id, pid}
  end

  def new_session() do
    id = System.unique_integer([:positive])
    DynamicSupervisor.start_child(__MODULE__, {NeoDnd.Session.Supervisor, id})
    [{pid, _}] = Registry.lookup(NeoDnd.Session.Registry, id)

    {id, pid}
  end

  @impl true
  def init(_) do
    DynamicSupervisor.init(strategy: :one_for_one)
  end
end
