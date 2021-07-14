defmodule NeoDnd.Session do
  use Supervisor

  def start_link(_) do
    Supervisor.start_link(__MODULE__, nil, name: __MODULE__)
  end

  @impl true
  def init(_) do
    children = [
      {Registry, keys: :unique, name: NeoDnd.Session.Registry},
      NeoDnd.Session.DynamicSupervisor
    ]

    Supervisor.init(children, strategy: :one_for_one)
  end
end
