using System;
using System.Collections.Generic;
using System.IO;
using System.Net.Http;
using System.Text.Json;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Media;
using System.Windows.Media.Imaging;

namespace FM26_ScoutClient
{
    public partial class MainWindow : Window
    {
        // Calea către folderul de resurse al serverului Spring Boot
        private string folderPath = @"C:\Apps\SCD\demo\demo\uploads";

        private readonly HttpClient _apiClient = new HttpClient();
        private List<JsonElement> _leagues = new List<JsonElement>();
        private List<JsonElement> _teams = new List<JsonElement>();
        private int _leagueIndex = 0;
        private int _teamIndex = 0;

        // Portul setat conform specificațiilor: 8083
        private const string BaseUrl = "http://localhost:8083/api";

        public MainWindow()
        {
            InitializeComponent();
            LoadInitialData();
        }

        private async void LoadInitialData()
        {
            try
            {
                var response = await _apiClient.GetStringAsync($"{BaseUrl}/league/all");
                _leagues = JsonSerializer.Deserialize<List<JsonElement>>(response);

                if (_leagues != null && _leagues.Count > 0)
                {
                    UpdateLeagueUI();
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show($"EROARE LIGI: {ex.Message}\nURL: {BaseUrl}/league/all");
            }
        }

        private async void LoadTeams(int leagueId)
        {
            try
            {
                string url = $"{BaseUrl}/team/league/{leagueId}";
                var response = await _apiClient.GetStringAsync(url);
                _teams = JsonSerializer.Deserialize<List<JsonElement>>(response);

                _teamIndex = 0;
                UpdateTeamUI();
            }
            catch (Exception ex)
            {
                _teams = new List<JsonElement>();
                TeamDisplay.Text = "FĂRĂ ECHIPE";
                System.Diagnostics.Debug.WriteLine($"Eroare echipe: {ex.Message}");
            }
        }

        private void UpdateLeagueUI()
        {
            if (_leagues == null || _leagues.Count == 0) return;
            try
            {
                var currentLeague = _leagues[_leagueIndex];
                LeagueDisplay.Text = currentLeague.GetProperty("name").GetString().ToUpper();
                int id = currentLeague.GetProperty("id").GetInt32();
                LoadTeams(id);
            }
            catch { LeagueDisplay.Text = "EROARE FORMAT DATE"; }
        }

        private void UpdateTeamUI()
        {
            if (_teams != null && _teams.Count > 0)
            {
                try { TeamDisplay.Text = _teams[_teamIndex].GetProperty("name").GetString().ToUpper(); }
                catch { TeamDisplay.Text = "EROARE NUME ECHIPĂ"; }
            }
            else { TeamDisplay.Text = "FĂRĂ ECHIPE"; }
        }

        // --- LOGICA PENTRU PALETA DE CULORI ȘI PENSULĂ ---

        private void SetColor_Click(object sender, RoutedEventArgs e)
        {
            if (sender is Button btn && btn.Background is SolidColorBrush brush)
            {
                // Modul radieră: dacă fundalul butonului este setat ca Transparent în XAML
                if (btn.Background == Brushes.Transparent)
                {
                    LogoCanvas.DefaultDrawingAttributes.Color = Colors.Black;
                }
                else
                {
                    LogoCanvas.DefaultDrawingAttributes.Color = brush.Color;
                }
            }
        }

        private void BrushSlider_ValueChanged(object sender, RoutedPropertyChangedEventArgs<double> e)
        {
            if (LogoCanvas != null)
            {
                LogoCanvas.DefaultDrawingAttributes.Width = e.NewValue;
                LogoCanvas.DefaultDrawingAttributes.Height = e.NewValue;
            }
        }

        // --- NAVIGARE ---

        private void PrevLeague_Click(object sender, RoutedEventArgs e)
        {
            if (_leagues.Count <= 1) return;
            _leagueIndex = (_leagueIndex - 1 + _leagues.Count) % _leagues.Count;
            UpdateLeagueUI();
        }

        private void NextLeague_Click(object sender, RoutedEventArgs e)
        {
            if (_leagues.Count <= 1) return;
            _leagueIndex = (_leagueIndex + 1) % _leagues.Count;
            UpdateLeagueUI();
        }

        private void PrevTeam_Click(object sender, RoutedEventArgs e)
        {
            if (_teams.Count <= 1) return;
            _teamIndex = (_teamIndex - 1 + _teams.Count) % _teams.Count;
            UpdateTeamUI();
        }

        private void NextTeam_Click(object sender, RoutedEventArgs e)
        {
            if (_teams.Count <= 1) return;
            _teamIndex = (_teamIndex + 1) % _teams.Count;
            UpdateTeamUI();
        }

        // --- LOGICA DESEN & SALVARE ---

        private void Clear_Click(object sender, RoutedEventArgs e)
        {
            LogoCanvas.Strokes.Clear();
        }

        private void SaveLogo_Click(object sender, RoutedEventArgs e)
        {
            if (_teams == null || _teams.Count == 0 || _teamIndex < 0)
            {
                MessageBox.Show("EROARE: Nu există nicio echipă selectată!");
                return;
            }

            try
            {
                // 1. Extragem ID-ul echipei pentru numele fișierului
                int teamId = _teams[_teamIndex].GetProperty("id").GetInt32();
                string teamName = _teams[_teamIndex].GetProperty("name").GetString();

                // 2. Procesăm desenul din Canvas
                int size = 280;
                RenderTargetBitmap rtb = new RenderTargetBitmap(size, size, 96d, 96d, PixelFormats.Pbgra32);
                rtb.Render(LogoCanvas);

                PngBitmapEncoder encoder = new PngBitmapEncoder();
                encoder.Frames.Add(BitmapFrame.Create(rtb));

                // 3. Construim calea fișierului folosind ID-ul (Exemplu: 10.png)
                string fileName = $"{teamId}.png";
                string filePath = Path.Combine(folderPath, fileName);

                // 4. Ne asigurăm că directorul există
                if (!Directory.Exists(folderPath))
                {
                    Directory.CreateDirectory(folderPath);
                }

                // 5. Scriem fișierul pe disc
                using (var stream = File.Create(filePath))
                {
                    encoder.Save(stream);
                }

                MessageBox.Show($"✅ TRANSMISIE REUȘITĂ!\nLogo salvat pentru: {teamName}\nID fișier: {fileName}");
            }
            catch (Exception ex)
            {
                MessageBox.Show($"EROARE LA EXPORT: {ex.Message}");
            }
        }
    }
}