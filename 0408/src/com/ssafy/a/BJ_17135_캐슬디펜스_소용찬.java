package com.ssafy.a;

import java.util.*;
import java.io.*;

public class BJ_17135_캐슬디펜스_소용찬 {
	
	  static int N, M, D;
	  static int[][] map;
	  static int enemies;
	  static int maxKill;
	  static int[] dx = { 0, -1, 0 }, dy = { -1, 0, 1 };

	public static void main(String[] args) throws IOException {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
	    StringTokenizer st = new StringTokenizer(br.readLine());

	    N = Integer.valueOf(st.nextToken());
	    M = Integer.valueOf(st.nextToken());
	    D = Integer.valueOf(st.nextToken());
	    enemies = 0;
	    maxKill = Integer.MIN_VALUE;

	    map = new int[N][M];

	    for (int i = 0; i < N; i++) {
	      st = new StringTokenizer(br.readLine());

	      for (int j = 0; j < M; j++) {
	        map[i][j] = Integer.valueOf(st.nextToken());

	        if (map[i][j] == 1) {
	          enemies += 1;
	        }
	      }
	    }
	    
	    setArcher(new boolean[M], 0, 3);
	    bw.write(maxKill + "\n");
	    bw.flush();
	    bw.close();

	}
	
	static void setArcher(boolean[] selected, int start, int pick) {
	    if (pick == 0) {
	      int[] archer = new int[3];
	      int idx = 0;

	      // 궁수가 있는 y 좌표 조합으로 구함
	      for (int i = 0; i < M; i++) {
	        if (selected[i])
	          archer[idx++] = i;
	      }

	      // 게임 시작
	      play(archer);
	      return;
	    }

	    for (int i = start; i < M; i++) {
	      selected[i] = true;
	      setArcher(selected, i + 1, pick - 1);
	      selected[i] = false;
	    }
	  }

	  static void play(int[] archer) {
	    int[][] playMap = copyMap();
	    boolean[][] died;
	    int totalKillCnt = 0;

	    for (int turn = 0; turn < N; turn++) {
	      // 하나의 적을 두명 이상의 궁수가 죽일 수 있으니(동시에 공격) 죽일 사람 표시하고 나중에 한꺼번에 죽임
	      died = new boolean[N][M];

	      // 1) 적 선택
	      for (int y : archer) {

	        // 1-1) 만약 거리가 1인 위치에 적이 존재하면 바로 선택
	        if (playMap[N - 1][y] == 1) {
	          died[N - 1][y] = true;
	        } else {
	          // 1-2) 거리가 1인 위치에서부터 거리 넓혀가면서 적 탐색
	          searchBFS(new Point(N - 1, y), died, playMap);
	        }

	      }

	      // 2) 적 죽이기
	      int killCnt = kill(died, playMap);

	      // 3) 몇명 죽였는지 기록
	      totalKillCnt += killCnt;

	      // 4) 적 이동
	      move(playMap);
	    }

	    // 5) 해당 궁수 위치에 대한 최대 죽일 수 있는 적의 수 갱신
	    maxKill = Math.max(maxKill, totalKillCnt);
	    return;
	  }

	  static int[][] copyMap() {
	    int[][] map2 = new int[N][M];

	    for (int i = 0; i < N; i++) {
	      System.arraycopy(map[i], 0, map2[i], 0, M);
	    }

	    return map2;
	  }

	  static void searchBFS(Point start, boolean[][] died, int[][] playMap) {
	    Queue<Point> queue = new LinkedList<Point>();
	    boolean[][] visited = new boolean[N][M];

	  
	    queue.add(start);
	    visited[start.x][start.y] = true;

	
	    for (int cnt = 1; cnt < D; cnt++) {
	      int size = queue.size();

	 
	      for (int j = 0; j < size; j++) {
	        int px = queue.peek().x;
	        int py = queue.poll().y;

	        for (int i = 0; i < 3; i++) {
	          int nx = px + dx[i];
	          int ny = py + dy[i];

	          if (!isIn(nx, ny) || visited[nx][ny])
	            continue;

	          
	          if (playMap[nx][ny] == 1) {
	            died[nx][ny] = true;
	            return;
	          }

	          
	          queue.add(new Point(nx, ny));
	          visited[nx][ny] = true;
	        }
	      }
	    }
	  }

	  static int kill(boolean[][] died, int[][] playMap) {
	    int cnt = 0;
	    for (int i = 0; i < N; i++) {
	      for (int j = 0; j < M; j++) {
	        if (died[i][j]) {
	          cnt++;
	          playMap[i][j] = 0;
	        }
	      }
	    }

	    return cnt;
	  }

	  static void move(int[][] playMap) {
	    
	    for (int i = N - 1; i >= 0; i--) {
	      for (int j = 0; j < M; j++) {
	        if (playMap[i][j] == 1) {
	          playMap[i][j] = 0;

	          if (i + 1 < N)
	            playMap[i + 1][j] = 1;
	        }
	      }
	    }
	  }

	  static boolean isIn(int x, int y) {
	    if (0 <= x && x < N && 0 <= y && y < M) {
	      return true;
	    }
	    return false;
	  }
	  
	  static class Point{
		  int x;
		  int y;
		  
		  public Point(int x, int y) {
			this.x = x;
			this.y = y;
		  }
	  }
}



