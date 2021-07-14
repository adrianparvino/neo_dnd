defmodule NeoDndWeb.SessionController do
  use NeoDndWeb, :controller

  def create(conn, _params) do
    {id, _pid} = NeoDnd.Session.DynamicSupervisor.new_session()

    id_token = Phoenix.Token.sign(conn, "session", id)

    conn
    |> put_session(:session_id, id_token)
    |> json(id)
  end

  def index(conn, _params) do
    id = get_session(conn, :session_id)

    with {:ok, id} <- Phoenix.Token.verify(conn, "session", id, max_age: 3600) do
      json(conn, id)
    else
      _ -> conn |> put_status(404) |> json(%{})
    end
  end

  def logout(conn, _params) do
    conn |> delete_session(:session_id) |> send_resp(204, "")
  end
end
