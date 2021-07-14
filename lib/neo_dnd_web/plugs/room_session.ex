defmodule NeoDndWeb.Plugs.RoomSession do
  import Plug.Conn

  def init(_), do: []

  def call(conn, _) do
    with id <- get_session(conn, :session_id),
         {:ok, id} <- Phoenix.Token.verify(conn, "session", id, max_age: 3600),
         [{pid, _}] <- Registry.lookup(NeoDnd.Session.Registry, id) do
      conn |> assign(:session_pid, pid)
    else
      _ ->
        conn |> send_resp(403, "") |> halt()
    end
  end
end
