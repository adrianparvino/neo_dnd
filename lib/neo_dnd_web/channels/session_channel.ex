defmodule NeoDndWeb.SessionChannel do
  use Phoenix.Channel

  def join("session", _message, socket) do
    {:ok, nil, socket}
  end

  def handle_in("new_session", _message, socket) do
    {:ok, nil, socket}
  end
end
