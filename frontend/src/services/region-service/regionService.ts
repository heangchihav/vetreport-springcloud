import { apiFetch } from "@/services/httpClient";

export interface Area {
  id: string;
  name: string;
  description?: string;
  createdAt: string;
  updatedAt: string;
}

export interface SubArea {
  id: string;
  name: string;
  description?: string;
  areaId: string;
  createdAt: string;
  updatedAt: string;
}

export interface Branch {
  id: string;
  name: string;
  description?: string;
  areaId: string;
  subAreaId: string;
  createdAt: string;
  updatedAt: string;
}

export interface AreaWithHierarchy {
  id: string;
  name: string;
  description?: string;
  createdAt: string;
  updatedAt: string;
  subAreas: SubAreaWithHierarchy[];
}

export interface SubAreaWithHierarchy {
  id: string;
  name: string;
  description?: string;
  areaId: string;
  createdAt: string;
  updatedAt: string;
  area?: Area;
  branches: BranchWithHierarchy[];
}

export interface BranchWithHierarchy {
  id: string;
  name: string;
  description?: string;
  areaId: string;
  subAreaId: string;
  createdAt: string;
  updatedAt: string;
  area?: Area;
  subArea?: SubArea;
}

export interface AreaPayload {
  name: string;
  description?: string;
}

export interface SubAreaPayload {
  name: string;
  description?: string;
  areaId: string;
}

export interface BranchPayload {
  name: string;
  description?: string;
  areaId: string;
  subAreaId: string;
}

export const regionService = {
  // Area operations
  async listAreas(): Promise<Area[]> {
    const response = await apiFetch("/api/region/areas");
    const result = await response.json();
    return result || [];
  },

  async getArea(id: string): Promise<Area> {
    const response = await apiFetch(`/api/region/areas/${id}`);
    const result = await response.json();
    return result;
  },

  async createArea(payload: AreaPayload): Promise<Area> {
    const response = await apiFetch("/api/region/areas", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload),
    });
    const result = await response.json();
    return result;
  },

  async updateArea(id: string, payload: AreaPayload): Promise<Area> {
    const response = await apiFetch(`/api/region/areas/${id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload),
    });
    const result = await response.json();
    return result;
  },

  async deleteArea(id: string): Promise<void> {
    await apiFetch(`/api/region/areas/${id}`, {
      method: "DELETE",
    });
  },

  // Sub Area operations
  async listSubAreas(): Promise<SubArea[]> {
    const response = await apiFetch("/api/region/sub-areas");
    const result = await response.json();
    return result || [];
  },

  async getSubArea(id: string): Promise<SubArea> {
    const response = await apiFetch(`/api/region/sub-areas/${id}`);
    const result = await response.json();
    return result;
  },

  async createSubArea(payload: SubAreaPayload): Promise<SubArea> {
    const response = await apiFetch("/api/region/sub-areas", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload),
    });
    const result = await response.json();
    return result;
  },

  async updateSubArea(id: string, payload: SubAreaPayload): Promise<SubArea> {
    const response = await apiFetch(`/api/region/sub-areas/${id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload),
    });
    const result = await response.json();
    return result;
  },

  async deleteSubArea(id: string): Promise<void> {
    await apiFetch(`/api/region/sub-areas/${id}`, {
      method: "DELETE",
    });
  },

  // Branch operations
  async listBranches(): Promise<Branch[]> {
    const response = await apiFetch("/api/region/branches");
    const result = await response.json();
    return result || [];
  },

  async getBranch(id: string): Promise<Branch> {
    const response = await apiFetch(`/api/region/branches/${id}`);
    const result = await response.json();
    return result;
  },

  async createBranch(payload: BranchPayload): Promise<Branch> {
    const response = await apiFetch("/api/region/branches", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload),
    });
    const result = await response.json();
    return result;
  },

  async updateBranch(id: string, payload: BranchPayload): Promise<Branch> {
    const response = await apiFetch(`/api/region/branches/${id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload),
    });
    const result = await response.json();
    return result;
  },

  async deleteBranch(id: string): Promise<void> {
    await apiFetch(`/api/region/branches/${id}`, {
      method: "DELETE",
    });
  },

  // Hierarchy operations
  async getAreas(): Promise<Area[]> {
    const response = await apiFetch("/api/branchreport/areas");
    const result = await response.json();
    return result.data?.data || [];
  },

  async getSubAreas(): Promise<SubArea[]> {
    const response = await apiFetch("/api/branchreport/sub-areas");
    const result = await response.json();
    return result.data?.data || [];
  },

  async getBranches(): Promise<Branch[]> {
    const response = await apiFetch("/api/branchreport/branches");
    const result = await response.json();
    return result.data?.data || [];
  },

  async getAreasWithHierarchy(): Promise<AreaWithHierarchy[]> {
    // Build hierarchy from separate calls
    const [areas, subAreas, branches] = await Promise.all([
      this.getAreas(),
      this.getSubAreas(),
      this.getBranches()
    ]);

    // Build hierarchy structure
    return areas.map((area: Area) => {
      const areaSubAreas = subAreas.filter((subArea: SubArea) => subArea.areaId === area.id);

      return {
        ...area,
        subAreas: areaSubAreas.map((subArea: SubArea) => {
          const subAreaBranches = branches.filter((branch: Branch) => branch.subAreaId === subArea.id);

          return {
            ...subArea,
            branches: subAreaBranches
          };
        })
      };
    });
  },
};
