defmodule NeoDndWeb.Router do
  use NeoDndWeb, :router

  pipeline :browser do
    plug :accepts, ["html"]
    plug :fetch_session
    plug :fetch_flash
    plug :protect_from_forgery
    plug :put_secure_browser_headers
  end

  pipeline :api do
    plug :accepts, ["json"]
    plug :fetch_session
  end

  pipeline :authorized_api do
    plug NeoDndWeb.Plugs.RoomSession
  end

  # Other scopes may use custom stacks.
  scope "/api", NeoDndWeb do
    pipe_through :api

    post "/session", SessionController, :create

    scope "/session" do
      pipe_through :authorized_api

      get "/", SessionController, :index
      post "/logout", SessionController, :logout

      resources "/:id/notes", NotesController, only: [:index, :create]
    end
  end

  # Enables LiveDashboard only for development
  #
  # If you want to use the LiveDashboard in production, you should put
  # it behind authentication and allow only admins to access it.
  # If your application does not have an admins-only section yet,
  # you can use Plug.BasicAuth to set up some basic authentication
  # as long as you are also using SSL (which you should anyway).
  if Mix.env() in [:dev, :test] do
    import Phoenix.LiveDashboard.Router

    scope "/" do
      pipe_through :browser
      live_dashboard "/dashboard", metrics: NeoDndWeb.Telemetry
    end
  end

  scope "/", NeoDndWeb do
    pipe_through :browser

    get "/*_x", PageController, :index
  end
end
