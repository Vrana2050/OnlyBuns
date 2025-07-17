import { Component, OnInit } from '@angular/core';
import { Chart } from 'chart.js/auto';
import { PostService } from '../service/post.service';

@Component({
  selector: 'app-app-analytics',
  templateUrl: './app-analytics.component.html',
  styleUrls: ['./app-analytics.component.css'],
})
export class AppAnalyticsComponent implements OnInit {
  activeTimePeriod: 'weekly' | 'monthly' | 'yearly' = 'weekly';
  timeSeriesChart: any;

  constructor(private postService: PostService) {}

  ngOnInit() {
    this.createTimeSeriesChart();
    this.loadData(this.activeTimePeriod);
    this.loadUserActivityStats();
  }

  loadData(period: 'weekly' | 'monthly' | 'yearly') {
    const { startDate, endDate } = this.getDateRange(period);

    this.postService.getActivityCounts(startDate, endDate).subscribe(
      (data) => {
        console.log('Activity counts:', data);
        this.updateChart(data); // Update the chart with the fetched data
      },
      (error) => {
        console.error('Error fetching activity counts:', error);
      }
    );
  }

  private getDateRange(period: 'weekly' | 'monthly' | 'yearly'): {
    startDate: Date;
    endDate: Date;
  } {
    const endDate = new Date();
    const startDate = new Date();

    switch (period) {
      case 'weekly':
        startDate.setDate(endDate.getDate() - 7);
        break;
      case 'monthly':
        startDate.setMonth(endDate.getMonth() - 1);
        break;
      case 'yearly':
        startDate.setFullYear(endDate.getFullYear() - 1);
        console.log('Start Date:', startDate, 'End Date:', endDate);
        break;
    }

    return { startDate, endDate };
  }

  onTimePeriodChange(period: 'weekly' | 'monthly' | 'yearly') {
    this.activeTimePeriod = period;
    this.loadData(period);
  }

  private updateChart(data: { posts: number; comments: number }) {
    if (this.timeSeriesChart) {
      const labels = [this.activeTimePeriod];
      this.timeSeriesChart.data.labels = labels;
      this.timeSeriesChart.data.datasets[0].data = [data.posts];
      this.timeSeriesChart.data.datasets[1].data = [data.comments];
      this.timeSeriesChart.update();
    }
  }

  private createTimeSeriesChart() {
    const ctx = document.getElementById('timeSeriesChart') as HTMLCanvasElement;
    this.timeSeriesChart = new Chart(ctx, {
      type: 'bar',
      data: {
        labels: [],
        datasets: [
          {
            label: 'Posts',
            data: [],
            backgroundColor: '#8884d8',
          },
          {
            label: 'Comments',
            data: [],
            backgroundColor: '#82ca9d',
          },
        ],
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
      },
    });
  }

  loadUserActivityStats() {
    this.postService.getUserActivityStats().subscribe(
      (stats) => {
        console.log('User Activity Stats:', stats);
        this.createRadialChart(stats);
      },
      (error) => {
        console.error('Error fetching stats', error);
      }
    );
  }
  radialChart: any;
  createRadialChart(data: {
    PostUsers: number;
    CommentUsers: number;
    InactiveUsers: number;
  }) {
    // Calculate total users to get percentages
    const totalUsers = data.PostUsers + data.CommentUsers + data.InactiveUsers;

    // Common options for all charts
    const chartOptions = {
      responsive: true,
      maintainAspectRatio: false,
      scales: {
        r: {
          min: 0,
          max: 100, // Set maximum to 100 for percentage
          ticks: {
            stepSize: 20,
          },
        },
      },
      plugins: {
        legend: {
          display: false,
        },
        tooltip: {
          callbacks: {
            label: function (context: any) {
              return `${context.raw.toFixed(1)}%`;
            },
          },
        },
      },
    };

    // Create chart for PostUsers
    const ctxPostUsers = document.getElementById(
      'postUsersChart'
    ) as HTMLCanvasElement;
    new Chart(ctxPostUsers, {
      type: 'polarArea',
      data: {
        labels: ['Posts'],
        datasets: [
          {
            data: [(data.PostUsers / totalUsers) * 100],
            backgroundColor: ['#42a5f5'],
          },
        ],
      },
      options: chartOptions,
    });

    // Create chart for CommentUsers
    const ctxCommentUsers = document.getElementById(
      'commentUsersChart'
    ) as HTMLCanvasElement;
    new Chart(ctxCommentUsers, {
      type: 'polarArea',
      data: {
        labels: ['Comments Only'],
        datasets: [
          {
            data: [(data.CommentUsers / totalUsers) * 100],
            backgroundColor: ['#66bb6a'],
          },
        ],
      },
      options: chartOptions,
    });

    // Create chart for InactiveUsers
    const ctxInactiveUsers = document.getElementById(
      'inactiveUsersChart'
    ) as HTMLCanvasElement;
    new Chart(ctxInactiveUsers, {
      type: 'polarArea',
      data: {
        labels: ['Inactive'],
        datasets: [
          {
            data: [(data.InactiveUsers / totalUsers) * 100],
            backgroundColor: ['#ff7043'],
          },
        ],
      },
      options: chartOptions,
    });
  }
}
