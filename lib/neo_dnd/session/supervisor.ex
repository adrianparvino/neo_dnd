defmodule NeoDnd.Session.Supervisor do
  use Supervisor

  def start_link(id) do
    Supervisor.start_link(__MODULE__, id)
  end

  @impl true
  def init(id) do
    children = [
      {NeoDnd.Session.Router, id}
    ]

    Supervisor.init(children, strategy: :one_for_one)
  end
end
